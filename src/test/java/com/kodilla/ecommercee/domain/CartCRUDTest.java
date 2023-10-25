

package com.kodilla.ecommercee.domain;

import com.kodilla.ecommercee.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @Transactional
    public void testCreateCart() {
        // Given
        Cart cart = new Cart();
        cart.setCreationDate(LocalDate.now());
        cart.setActive(true);

        Product product1 = new Product();
        product1.setNameProduct("Product 1");
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setNameProduct("Product 2");
        productRepository.save(product2);

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        cart.setProductsList(products);

        // When
        cartRepository.save(cart);

        // Then
        Cart savedCart = cartRepository.findById(cart.getCartId()).orElse(null);
        assertNotNull(savedCart);
        assertTrue(savedCart.isActive());
        assertEquals(2, savedCart.getProductsList().size());
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
