package com.example.todoapp.repositories;

import com.example.todoapp.models.Cart;
import com.example.todoapp.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // ✅ Fetch all cart items for a specific cart
    List<CartItem> findByCart(Cart cart);

    // ✅ Delete all cart items for a specific cart (if needed)
    void deleteByCart(Cart cart);
}
