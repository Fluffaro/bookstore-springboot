package com.example.todoapp.services;

import com.example.todoapp.models.Cart;
import com.example.todoapp.models.CartItem;
import com.example.todoapp.models.Task;
import com.example.todoapp.models.User;
import com.example.todoapp.repositories.CartItemRepository;
import com.example.todoapp.repositories.CartRepository;
import com.example.todoapp.repositories.TaskRepository;
import com.example.todoapp.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       TaskRepository taskRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart(user);
            return cartRepository.save(newCart);
        });
    }

    public Cart getCartByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return getCartByUser(user);
    }

    // ✅ Add Book to Cart (Handles Quantity)
    @Transactional
    public Cart addToCart(String username, Long bookId, int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task book = taskRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Cart cart = getCartByUser(user);

        // Check if book is already in the cart
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Increase quantity
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            // Add new item to cart
            CartItem newItem = new CartItem(cart, book, quantity);
            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        return cartRepository.save(cart);
    }

    // ✅ Get All Items in User's Cart
    public List<CartItem> getCartItems(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getCartByUser(user);
        return cartItemRepository.findByCart(cart);
    }

    // ✅ Remove a Book from Cart
    @Transactional
    public void removeFromCart(String username, Long bookId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getCartByUser(user);

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            throw new RuntimeException("Book not found in cart");
        }
    }
}
