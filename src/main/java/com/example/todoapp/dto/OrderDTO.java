package com.example.todoapp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private List<OrderItemDTO> orderItems;

    public OrderDTO(Long id, LocalDateTime orderDate, BigDecimal totalAmount, List<OrderItemDTO> orderItems) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }

    // ✅ Getters and Setters
    public Long getId() { return id; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public List<OrderItemDTO> getOrderItems() { return orderItems; }
}
