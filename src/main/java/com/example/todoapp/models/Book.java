package com.example.todoapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "books") // Explicitly naming the table
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required") // ✅ Prevents null and empty values
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author is required") // ✅ Prevents null and empty values
    @Column(nullable = false)
    private String author;

    @NotNull(message = "Price is required") // ✅ Prevents null
    @DecimalMin(value = "0.01", message = "Price must be greater than 0") // ✅ Ensures price > 0
    @Column(nullable = false)
    private BigDecimal price;

    @NotBlank(message = "Description is required") // ✅ Prevents null and empty values
    @Column(nullable = false, length = 1000) // Allows longer descriptions
    private String description;

    @NotBlank(message = "Cover image URL is required") // ✅ Prevents null and empty values
    @Column(nullable = false)
    private String coverImage;

    @NotBlank(message = "Category is required") // ✅ Prevents null and empty values
    @Column(nullable = false)
    private String category;

    @NotNull // ✅ Prevents null (default value = false)
    @Column(nullable = false)
    private Boolean featured = false;

    // Default constructor (needed by JPA)
    public Book() {}

    // Constructor (without ID, since it's auto-generated)
    public Book(String title, String author, BigDecimal price, String description, String coverImage, String category, Boolean featured) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.coverImage = coverImage;
        this.category = category;
        this.featured = featured;
    }
}
