package com.example.todoapp.controllers;

import com.example.todoapp.dto.OrderDTO;
import com.example.todoapp.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ✅ Place Order (Checkout) - Now returns OrderDTO
    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> placeOrder(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(403).build();
        }

        String username = principal.getName();
        OrderDTO order = orderService.placeOrder(username);
        return ResponseEntity.ok(order);
    }


    // ✅ Get User's Order History - Now returns List<OrderDTO>
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getUserOrders(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(403).build();
        }

        String username = principal.getName();
        List<OrderDTO> orders = orderService.getUserOrders(username);
        return ResponseEntity.ok(orders);
    }
}
