package com.kodilla.ecommercee.domain;

import com.kodilla.ecommercee.domain.Cart;
import com.kodilla.ecommercee.domain.Product;
import com.kodilla.ecommercee.domain.User;
import com.kodilla.ecommercee.repository.CartRepository;
import com.kodilla.ecommercee.repository.ProductRepository;
import com.kodilla.ecommercee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import com.kodilla.ecommercee.repository.*
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CartCRUDTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        productRepository.deleteAll();
        cartRepository.deleteAll();
    }

    @Test
    @DirtiesContext
    public void testCreateCart() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        User savedUser = userRepository.save(user);

        Product product1 = new Product();
        product1.setName("Product 1");
        Product product2 = new Product();
        product2.setName("Product 2");
        List<Product> products = Arrays.asList(product1, product2);
        productRepository.saveAll(products);

        Cart cart = new Cart();
        cart.setUser(savedUser);
        cart.setCreationDate(LocalDate.now());
        cart.setActive(true);
        cart.setProductsList(products);

        // When
        Cart savedCart = cartRepository.save(cart);
        Cart foundCart = cartRepository.findById(savedCart.getCartId()).orElse(null);

        // Then
        assertNotNull(savedCart.getCartId());
        assertNotNull(foundCart);
        assertEquals(savedCart.getCartId(), foundCart.getCartId());
        assertTrue(foundCart.isActive());
        assertEquals(2, foundCart.getProductsList().size());
        assertEquals("Product 1", foundCart.getProductsList().get(0).getName());
        assertEquals("Product 2", foundCart.getProductsList().get(1).getName());
    }

    @Test
    @DirtiesContext
    public void testUpdateCart() {
        // Given
        Cart cart = new Cart();
        cart.setCreationDate(LocalDate.now());
        cart.setActive(true);
        cart = cartRepository.save(cart);

        // When
        cart.setActive(false);
        cartRepository.save(cart);
        Cart updatedCart = cartRepository.findById(cart.getCartId()).orElse(null);

        // Then
        assertNotNull(updatedCart);
        assertFalse(updatedCart.isActive());
    }

    @Test
    @DirtiesContext
    public void testDeleteCart() {
        // Given
        Cart cart = new Cart();
        cart.setCreationDate(LocalDate.now());
        cart.setActive(true);
        cart = cartRepository.save(cart);

        // When
        cartRepository.delete(cart);

        // Then
        Cart deletedCart = cartRepository.findById(cart.getCartId()).orElse(null);
        assertNull(deletedCart);
    }
}
