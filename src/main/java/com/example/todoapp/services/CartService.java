package com.example.todoapp.services;

import com.example.todoapp.models.Cart;
import com.example.todoapp.models.CartItem;
import com.example.todoapp.models.Book;
import com.example.todoapp.models.User;
import com.example.todoapp.repositories.CartItemRepository;
import com.example.todoapp.repositories.CartRepository;
import com.example.todoapp.repositories.BookRepository;
import com.example.todoapp.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       BookRepository bookRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }



    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart(user);
            return cartRepository.save(newCart);
        });
    }
    @Transactional
    public Cart updateCartItem(String username, Long bookId, int newQuantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getCartByUser(user);

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            if (newQuantity <= 0) {
                cart.getCartItems().remove(cartItem);
                cartItemRepository.delete(cartItem);
            } else {
                cartItem.setQuantity(newQuantity);
                cartItemRepository.save(cartItem);
            }
        } else {
            throw new RuntimeException("Book not found in cart");
        }

        return cartRepository.save(cart);
    }


    // ✅ Clear all items from the cart
    @Transactional
    public void clearCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getCartByUser(user);

        // Delete all items from the cart
        cartItemRepository.deleteByCart(cart);

        // Clear the cart list (ensures cache/state updates)
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }


    public Cart getCartByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return getCartByUser(user);
    }

    // ✅ Add Book to Cart (Handles Quantity)
    @Transactional
    public Cart addToCart(String username, Long bookId, int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Cart cart = getCartByUser(user);

        // Check if book is already in the cart
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Increase quantity
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            // Add new item to cart
            CartItem newItem = new CartItem(cart, book, quantity);
            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        return cartRepository.save(cart);
    }

    // ✅ Get All Items in User's Cart
    public List<CartItem> getCartItems(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getCartByUser(user);
        return cartItemRepository.findByCart(cart);
    }

    // ✅ Remove a Book from Cart
    @Transactional
    public void removeFromCart(String username, Long bookId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getCartByUser(user);

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            throw new RuntimeException("Book not found in cart");
        }
    }
}
