package com.example.todoapp.controllers;

import com.example.todoapp.models.Book;
import com.example.todoapp.services.BookService;
import jakarta.validation.Valid; // ✅ Import for validation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // ✅ Import for better HTTP responses
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // ✅ Get all books

    @GetMapping
    public List<Book> getAllBooks(@RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return bookService.getBooksByCategory(category);
        }
        return bookService.getAllBooks();
    }

    // ✅ Get a single book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok) // Return 200 OK if found
                .orElseGet(() -> ResponseEntity.notFound().build()); // Return 404 if not found
    }

    // ✅ Create a new book
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.createBook(book);
        return ResponseEntity.ok(savedBook); // 200 OK with the saved book
    }

    // ✅ Update an existing book
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        return bookService.updateBook(id, book)
                .map(ResponseEntity::ok) // Return updated book if found
                .orElseGet(() -> ResponseEntity.notFound().build()); // Return 404 if not found
    }

    // ✅ Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookService.deleteBook(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content if deleted
        }
        return ResponseEntity.notFound().build(); // 404 if not found
    }
}
