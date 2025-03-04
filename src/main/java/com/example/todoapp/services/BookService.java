package com.example.todoapp.services;

import com.example.todoapp.models.Book;
import com.example.todoapp.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ✅ Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // ✅ Get a single book by ID
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // ✅ Create a new book
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // ✅ Update an existing book
    public Optional<Book> updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id).map(existingBook -> {
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setPrice(updatedBook.getPrice());
            existingBook.setDescription(updatedBook.getDescription());
            existingBook.setCoverImage(updatedBook.getCoverImage());
            existingBook.setCategory(updatedBook.getCategory());
            existingBook.setFeatured(updatedBook.getFeatured());
            return bookRepository.save(existingBook);
        });
    }
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category);
    }
    // ✅ Delete a book
    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
