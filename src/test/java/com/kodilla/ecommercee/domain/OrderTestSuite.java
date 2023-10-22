package com.kodilla.ecommercee.domain;

import com.kodilla.ecommercee.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class OrderTestSuite {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testCreateOrder() {
        //Given
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setStatus("new");
        //When
        orderRepository.save(order);
        //Then
        Long id = order.getOrderId();
        Optional<Order> readOrder = orderRepository.findById(id);
        Assertions.assertThat(readOrder).isPresent();
        //CleanUp
        orderRepository.deleteById(id);
    }

    @Test
    public void testReadOrder() {
        //Given
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setStatus("new");
        //When
        orderRepository.save(order);
        //Then
        Long id = order.getOrderId();
        Optional<Order> readOrder = orderRepository.findById(id);
        Assertions.assertThat(readOrder).isPresent();
        //CleanUp
        orderRepository.deleteById(id);
    }

    @Test
    public void testUpdateOrder() {
        //Given
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setStatus("new");
        //When
        orderRepository.save(order);
        //Then
        Long id = order.getOrderId();
        Optional<Order> readOrder = orderRepository.findById(id);
        Assertions.assertThat(readOrder).isPresent();
        //CleanUp
        orderRepository.deleteById(id);
    }

    @Test
    public void testDeleteOrder() {
        //Given
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setStatus("new");
        //When
        orderRepository.save(order);
        //Then
        Long id = order.getOrderId();
        Optional<Order> readOrder = orderRepository.findById(id);
        Assertions.assertThat(readOrder).isPresent();
        //CleanUp
        orderRepository.deleteById(id);
    }
}
