package com.example.todoapp.services;

import com.example.todoapp.dto.OrderDTO;
import com.example.todoapp.dto.OrderItemDTO;
import com.example.todoapp.models.*;
import com.example.todoapp.repositories.*;
import com.example.todoapp.services.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        CartService cartService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderDTO placeOrder(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartService.getCartByUser(user);
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(BigDecimal.ZERO.doubleValue()); // ✅ Convert BigDecimal to double


        // Convert cart items to order items
        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            return orderItem;
        }).collect(Collectors.toList());

        order.setTotalAmount(orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue()); // ✅ Convert BigDecimal to double

        order.setOrderItems(orderItems);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        // Clear user's cart after checkout
        cartService.clearCart(user.getUsername());

        // ✅ Convert to OrderDTO before returning
        return new OrderDTO(
                order.getId(),
                order.getOrderDate(),
                BigDecimal.valueOf(order.getTotalAmount()), //

                orderItems.stream().map(item ->
                        new OrderItemDTO(
                                item.getId(),
                                item.getBook().getTitle(),
                                item.getBook().getAuthor(),
                                item.getPrice(),
                                item.getQuantity()
                        )
                ).collect(Collectors.toList())
        );
    }


    public List<OrderDTO> getUserOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderRepository.findByUser(user);

        // ✅ Convert List<Order> to List<OrderDTO>
        return orders.stream().map(order ->
                new OrderDTO(
                        order.getId(),
                        order.getOrderDate(),
                        BigDecimal.valueOf(order.getTotalAmount()),

                        order.getOrderItems().stream().map(item ->
                                new OrderItemDTO(
                                        item.getId(),
                                        item.getBook().getTitle(),
                                        item.getBook().getAuthor(),
                                        item.getPrice(),
                                        item.getQuantity()
                                )
                        ).collect(Collectors.toList())
                )
        ).collect(Collectors.toList());
    }
}
