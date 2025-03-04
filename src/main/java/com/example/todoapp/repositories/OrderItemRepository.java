package com.example.todoapp.repositories;

import com.example.todoapp.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
