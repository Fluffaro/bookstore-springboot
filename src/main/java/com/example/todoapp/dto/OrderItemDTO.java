package com.example.todoapp.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Long id;
    private String bookTitle;
    private String bookAuthor;
    private BigDecimal price;
    private int quantity;

    public OrderItemDTO(Long id, String bookTitle, String bookAuthor, BigDecimal price, int quantity) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.price = price;
        this.quantity = quantity;
    }

    // ✅ Getters and Setters
    public Long getId() { return id; }
    public String getBookTitle() { return bookTitle; }
    public String getBookAuthor() { return bookAuthor; }
    public BigDecimal getPrice() { return price; }
    public int getQuantity() { return quantity; }
}
