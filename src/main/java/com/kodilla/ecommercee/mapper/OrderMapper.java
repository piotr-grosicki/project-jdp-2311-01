package com.kodilla.ecommercee.mapper;

import com.kodilla.ecommercee.domain.Order;
import com.kodilla.ecommercee.domain.OrderDto;
import com.kodilla.ecommercee.domain.Product;
import com.kodilla.ecommercee.repository.CartRepository;
import com.kodilla.ecommercee.repository.ProductRepository;
import com.kodilla.ecommercee.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.kodilla.ecommercee.exception.UserNotFoundException;
import com.kodilla.ecommercee.exception.CartNotFoundException;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMapper {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;


    public Order mapToOrder(final OrderDto orderDto)  {
        Order order = new Order();
        order.setOrderId(orderDto.getOrderId());
        order.setUser(userRepository.findById(orderDto.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found with ID: "+ orderDto.getUserId())));
        order.setOrderDate(orderDto.getOrderDate());
        order.setStatus(orderDto.getStatus());
        List<Product> productList = productRepository.findAllById(orderDto.getProductId());
        order.setProductList(productList);
        order.setCart(cartRepository.findById(orderDto.getCartId()).orElseThrow(CartNotFoundException::new));
        return order;
    }

    public OrderDto mapToOrderDto(final Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setUserId(order.getUser().getId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setStatus(order.getStatus());
        List<Long> productIdList = order.getProductList()
                .stream()
                .map(Product::getProductId)
                .collect(Collectors.toList());
        orderDto.setProductId(productIdList);
        orderDto.setCartId(order.getCart().getCartId());
        return orderDto;
    }

    public List<OrderDto> mapToOrderDtoList(final List<Order> orderList) {
        return orderList.stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }

    public List<Order> mapToOrderList(final List<OrderDto> orderDtoList) {
        return orderDtoList.stream()
                .map(this::mapToOrder)
                .collect(Collectors.toList());
    }
}