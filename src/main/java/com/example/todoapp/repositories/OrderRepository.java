package com.example.todoapp.repositories;

import com.example.todoapp.models.Order;
import com.example.todoapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
