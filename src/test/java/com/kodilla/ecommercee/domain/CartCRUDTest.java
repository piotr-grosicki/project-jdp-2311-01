package com.kodilla.ecommercee.domain;

import com.kodilla.ecommercee.repository.CartRepository;
import com.kodilla.ecommercee.repository.OrderRepository;
import com.kodilla.ecommercee.repository.ProductRepository;
import com.kodilla.ecommercee.repository.UserRepository;
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
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Test
    @Transactional
    @DirtiesContext
    public void testReadCart() {
        // Given
        Cart cart = new Cart();
        cart.setCreationDate(LocalDate.now());
        cart.setActive(true);

        // When
        cartRepository.save(cart);
        Long cartId = cart.getCartId();

        // Then
        Cart retrievedCart = cartRepository.findById(cartId).orElse(null);
        assertNotNull(retrievedCart);
        assertEquals(cartId, retrievedCart.getCartId());
        assertTrue(retrievedCart.isActive());
    }

    @Test
    @Transactional
    @DirtiesContext
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

    @Test
    @Transactional
    @DirtiesContext
    public void testCartProductRelations() {
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

        // Add products to the cart
        cart.getProductsList().add(product1);
        cart.getProductsList().add(product2);

        // When
        cartRepository.save(cart);

        // Then
        Cart savedCart = cartRepository.findById(cart.getCartId()).orElse(null);
        assertNotNull(savedCart);
        assertTrue(savedCart.isActive());
        assertEquals(2, savedCart.getProductsList().size());

        for (Product product : savedCart.getProductsList()) {
            assertNotNull(product);
        }
    }



    @Test
    @Transactional
    @DirtiesContext
    public void testUserCartRelationship() {
        // Given
        User user = new User();
        user.setUsername("username");
        user.setPassword("Password");

        Cart cart = new Cart();
        cart.setActive(true);
        cart.setUser(user);

        user.getCartList().add(cart);

        // When
        userRepository.save(user);
        cartRepository.save(cart);
        User savedUser = userRepository.findById(user.getId()).orElse(null);
        Cart savedCart = cartRepository.findById(cart.getCartId()).orElse(null);

        // Then
        assertEquals(1, savedUser.getCartList().size());
        assertEquals("username", savedCart.getUser().getUsername());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testCartOrderRelationship() {
        // Given
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user = userRepository.save(user);

        Cart cart = new Cart();
        cart.setActive(true);
        cart.setUser(user);
        cart = cartRepository.save(cart);

        Product product = new Product();
        product.setNameProduct("Product 1");
        product.setPrice(10.0);
        product = productRepository.save(product);

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        cart.setProductsList(productList);
        cartRepository.save(cart);

        Order order = new Order();
        order.setUser(user);
        order.setStatus("Pending");
        order.setProductList(productList);
        order = orderRepository.save(order);

        // When
        Cart savedCart = cartRepository.findById(cart.getCartId()).orElse(null);
        Order savedOrder = orderRepository.findById(order.getOrderId()).orElse(null);

        // Then
        assertNotNull(savedCart);
        assertNotNull(savedOrder);
        assertEquals(savedCart.getUser(), user);
        assertEquals(savedOrder.getUser(), user);

        List<Product> cartProducts = savedCart.getProductsList();
        List<Product> orderProducts = savedOrder.getProductList();
        assertNotNull(cartProducts);
        assertNotNull(orderProducts);
        assertEquals(cartProducts.size(), orderProducts.size());
        for (int i = 0; i < cartProducts.size(); i++) {
            assertEquals(cartProducts.get(i), orderProducts.get(i));
        }
    }
}
