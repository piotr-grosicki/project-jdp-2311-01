package com.kodilla.ecommercee.domain;

import com.kodilla.ecommercee.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderEntityTestSuite {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @Transactional
    @DirtiesContext
    public void setup() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setBlocked(false);
        user.setToken(12345L);

        Cart cart = new Cart();
        cart.setCreationDate(LocalDate.of(2023, 3, 20));
        cart.setActive(true);
        cart.setUser(user);

        Product product = new Product();
        product.setNameProduct("Product1");
        product.setDescriptionProduct("Description1");
        product.setPrice(100.0);

        cart.getProductsList().add(product);

        Order order = new Order();
        order.setOrderDate(new Date());
        order.setStatus("New");
        order.setUser(user);

        userRepository.save(user);
        productRepository.save(product);
        cartRepository.save(cart);
        orderRepository.save(order);
    }

    @Test
    @Transactional
    @DirtiesContext
    public void shouldCreateOrder() {
        List<Order> orders = orderRepository.findAll();

        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    public void shouldFindOrdersById() {
        Optional<Order> readOrder = orderRepository.findById(1L);
        assertThat(readOrder.isPresent()).isTrue();
    }

    @Test
    @Transactional
    @DirtiesContext
    public void shouldFindAllOrders() {
        List<Order> orders = orderRepository.findAll();
        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    public void shouldUpdateOrder() {
        Optional<Order> readOrder = orderRepository.findById(1L);
        Order order = readOrder.get();
        order.setStatus("Paid");
        orderRepository.save(order);
        assertThat(order.getStatus()).isEqualTo("Paid");
    }

    @Test
    @Transactional
    @DirtiesContext
    public void shouldDeleteOrderById() {
        orderRepository.deleteById(1L);
        assertThat(orderRepository.existsById(1L)).isFalse();
    }

    @Test
    @Transactional
    @DirtiesContext
    public void shouldCreateOrderWithProductFromCart() {
        Optional<Order> readOrder = orderRepository.findById(1L);
        assertThat(readOrder.isPresent()).isTrue();

        // Pobieranie koszyka z zamówieniami
        Optional<Cart> cart = cartRepository.findById(1L);
        assertThat(cart.isPresent()).isTrue();

        // Sprawdanie czy koszyk zawiera produkt
        assertThat(cart.get().getProductsList().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    public void shouldDeleteOrderWithProductsFromCart() {
        // Odszukanie zamówienia
        Optional<Order> readOrder = orderRepository.findById(1L);
        assertThat(readOrder.isPresent()).isTrue();

        // Pobieranie koszyka z zamówieniem
        Optional<Cart> cart = cartRepository.findById(1L);
        assertThat(cart.isPresent()).isTrue();

        // Sprawdzenie, czy koszyk zawiera produkt
        assertThat(cart.get().getProductsList().size()).isEqualTo(1);

        // Usunięcie zamówienia
        orderRepository.deleteById(1L);

        // Sprawdzenie, czy zamówienie zostało usunięte
        assertThat(orderRepository.existsById(1L)).isFalse();
    }

    @Test
    @Transactional
    @DirtiesContext
    public void shouldDeleteOrderAndKeepProductsInCart() {
        // Odszukanie zamówienia
        Optional<Order> readOrder = orderRepository.findById(1L);
        assertThat(readOrder.isPresent()).isTrue();

        // Pobieranie koszyka z zamówieniem
        Optional<Cart> cart = cartRepository.findById(1L);
        assertThat(cart.isPresent()).isTrue();

        // Sprawdzenie, czy koszyk zawiera produkt
        assertThat(cart.get().getProductsList().size()).isEqualTo(1);

        // Usunięcie zamówienia, ale zachowanie produktów w koszyku
        Order order = readOrder.get();
        orderRepository.delete(order);

        // Sprawdzenie, czy zamówienie zostało usunięte
        assertThat(orderRepository.existsById(1L)).isFalse();

        // Ponowne pobranie koszyka
        Optional<Cart> updatedCart = cartRepository.findById(1L);

        // Sprawdzenie, czy koszyk nadal zawiera produkt
        assertThat(updatedCart.isPresent()).isTrue();
        assertThat(updatedCart.get().getProductsList().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    public void shouldDeleteOrderButKeepUser() {
        // Odszukanie zamówienia
        Optional<Order> readOrder = orderRepository.findById(1L);
        assertThat(readOrder.isPresent()).isTrue();

        // Pobieranie użytkownika przypisanego do zamówienia
        User user = readOrder.get().getUser();
        assertThat(user).isNotNull();

        // Usunięcie zamówienia, ale pozostawienie użytkownika
        orderRepository.deleteById(1L);

        // Sprawdzenie, czy zamówienie zostało usunięte
        assertThat(orderRepository.existsById(1L)).isFalse();

        // Sprawdzenie, czy użytkownik nadal istnieje
        assertThat(userRepository.existsById(user.getId())).isTrue();
    }
}
