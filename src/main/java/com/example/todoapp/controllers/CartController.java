package com.example.todoapp.controllers;

import com.example.todoapp.dto.CartItemRequest;
import com.example.todoapp.models.Cart;
import com.example.todoapp.models.CartItem;
import com.example.todoapp.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // ✅ Add Book to Cart with Quantity Support
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(403).body("Unauthorized - No user found");
        }

        String username = principal.getName();
        Long bookId = request.getBookId();
        int quantity = request.getQuantity();

        Cart updatedCart = cartService.addToCart(username, bookId, quantity);
        return ResponseEntity.ok(updatedCart);
    }


    // ✅ Get Cart Items (Including Quantity)
    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(Principal principal) {
        String username = principal.getName();
        List<CartItem> cartItems = cartService.getCartItems(username);
        return ResponseEntity.ok(cartItems);
    }

    // ✅ Remove a Book from Cart
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestParam Long bookId, Principal principal) {
        String username = principal.getName();
        cartService.removeFromCart(username, bookId);
        return ResponseEntity.ok("Book removed from cart!");
    }
}
