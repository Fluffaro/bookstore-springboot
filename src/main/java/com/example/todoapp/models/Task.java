package com.example.todoapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "tasks") // Explicitly naming the table
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private BigDecimal price;
    private String description;
    private String coverImage;
    private String category;
    private Boolean featured;


    // Default constructor
    public Task() {
    }

    // Constructor with all fields
    public Task(String title, String author, BigDecimal price, String description, String coverImage, String category, Boolean featured, Boolean completed) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.coverImage = coverImage;
        this.category = category;
        this.featured = featured;

    }
}
