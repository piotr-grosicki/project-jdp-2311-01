package com.kodilla.ecommercee.service;

import com.kodilla.ecommercee.domain.Order;
import com.kodilla.ecommercee.domain.OrderDto;
import com.kodilla.ecommercee.exception.OrderNotFoundException;
import com.kodilla.ecommercee.mapper.OrderMapper;
import com.kodilla.ecommercee.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.mapToOrderDtoList(orders);
    }

    public OrderDto getOrderById(final Long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        return orderMapper.mapToOrderDto(order);
    }

    public OrderDto createOrder(final OrderDto orderDto) {
        Order order = orderMapper.mapToOrder(orderDto);
        Order saveOrder = orderRepository.save(order);
        return orderMapper.mapToOrderDto(saveOrder);
    }

    public OrderDto updateOrder(final OrderDto orderDto) throws OrderNotFoundException {
        orderRepository.findById(orderDto.getOrderId()).orElseThrow(OrderNotFoundException::new);
        Order order = orderMapper.mapToOrder(orderDto);
        Order updateOrder = orderRepository.save(order);
        return orderMapper.mapToOrderDto(updateOrder);
    }

    public void deleteOrder(final Long orderId) throws OrderNotFoundException {
        try {
            orderRepository.deleteById(orderId);
        } catch (Exception e) {
            throw new OrderNotFoundException();
        }
    }
}
