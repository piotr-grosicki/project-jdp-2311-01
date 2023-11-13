package com.kodilla.ecommercee.service;

import com.kodilla.ecommercee.domain.*;
import com.kodilla.ecommercee.exception.OrderNotFoundException;
import com.kodilla.ecommercee.repository.CartRepository;
import com.kodilla.ecommercee.repository.OrderRepository;
import com.kodilla.ecommercee.repository.ProductRepository;
import com.kodilla.ecommercee.repository.UserRepository;
import com.kodilla.ecommercee.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;

    private User user;
    private Cart cart;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setBlocked(false);
        user.setToken(12345L);

        cart = new Cart();
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
        order.setCart(cart);

        userRepository.save(user);
        productRepository.save(product);
        cartRepository.save(cart);
        orderRepository.save(order);

    }

    @Test
    @Transactional
    @DirtiesContext
    public void testGetAllOrders() {
        //Given
        Order order1 = new Order();
        order1.setOrderDate(new Date());
        order1.setStatus("Send");
        order1.setUser(user);
        order1.setCart(cart);
        orderRepository.save(order1);

        //When
        List<OrderDto> orders = orderService.getAllOrders();

        //Then
        assertThat(orders.size()).isEqualTo(2);
        assertEquals("New",orders.get(0).getStatus());
        assertEquals("Send",orders.get(1).getStatus());
    }
    @Test
    @DirtiesContext
    @Transactional
    public void testGetOrderByIdWhenOrderNotExists() {
        // When and Then
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(99L));
    }

    @Test
    @DirtiesContext
    @Transactional
    public void testCreateAndReadOrder() throws OrderNotFoundException {
        //Given
        OrderDto newOrder = new OrderDto();
        newOrder.setOrderDate(new Date());
        newOrder.setStatus("New order");
        newOrder.setUserId(user.getId());
        newOrder.setCartId(cart.getCartId());

        //When
        OrderDto createOrder = orderService.createOrder(newOrder);

        //Then
        assertNotNull(createOrder.getOrderId());
        OrderDto readOrder = orderService.getOrderById(createOrder.getOrderId());
        assertNotNull(readOrder);
        assertEquals(newOrder.getStatus(),readOrder.getStatus());

    }

    @Test
    @DirtiesContext
    @Transactional
    public void testUpdateOrder() throws OrderNotFoundException {
        //Given
        OrderDto newOrder = new OrderDto();
        newOrder.setOrderDate(new Date());
        newOrder.setStatus("New order");
        newOrder.setUserId(user.getId());
        newOrder.setCartId(cart.getCartId());

        //When
        OrderDto createOrder = orderService.createOrder(newOrder);
        createOrder.setStatus("Something new");
        orderService.updateOrder(createOrder);
        OrderDto updateOrder = orderService.getOrderById(createOrder.getOrderId());

        //Then
        assertEquals("Something new",updateOrder.getStatus());
    }

    @Test
    @DirtiesContext
    @Transactional
    public void testDeleteOrder() throws OrderNotFoundException {
        //Given
        OrderDto newOrder = new OrderDto();
        newOrder.setOrderDate(new Date());
        newOrder.setStatus("New order");
        newOrder.setUserId(user.getId());
        newOrder.setCartId(cart.getCartId());

        //When
        OrderDto createOrder = orderService.createOrder(newOrder);

        //Then
        assertNotNull(createOrder.getOrderId());
        orderService.deleteOrder(createOrder.getOrderId());
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(createOrder.getOrderId()));
    }
}