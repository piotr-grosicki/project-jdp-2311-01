package com.kodilla.ecommercee.mapper;

import com.kodilla.ecommercee.domain.Order;
import com.kodilla.ecommercee.domain.OrderDto;
import com.kodilla.ecommercee.domain.Product;
import com.kodilla.ecommercee.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMapper {



    private UserService userService;
    private ProductMapper productMapper;
    private ProductService productService;



    public Order mapToOrder(final OrderDto orderDto) throws UserNotFoundException {
        Order order = new Order();
        order.setOrderId(orderDto.getOrderId());
        User user = userService.getUserById(orderDto.getUserId());
        order.setUser(user);
        order.setOrderDate(orderDto.getOrderDate());
        order.setStatus(orderDto.getStatus());
        List<Product> productList = productService.getProductById(orderDto.getProductId());
        order.setProductList(productList);
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
