package com.kodilla.ecommercee.mapper;

import com.kodilla.ecommercee.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductMapper {

    public static ProductDto mapToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getProductId());
        productDto.setName(product.getNameProduct());
        productDto.setDescription(product.getDescriptionProduct());
        productDto.setPrice(product.getPrice());

        if (product.getGroup() != null) {
            productDto.setGroup_id(product.getGroup().getGroupId());
        }

        if (product.getCartList() != null) {
            List<Long> cartIds = product.getCartList().stream()
                    .map(cart -> cart.getCartId())
                    .collect(Collectors.toList());
            productDto.setCartDtoList(cartIds);
        }

        if (product.getOrderList() != null) {
            List<Long> orderIds = product.getOrderList().stream()
                    .map(order -> order.getOrderId())
                    .collect(Collectors.toList());
            productDto.setOrderDtoList(orderIds);
        }
        return productDto;
    }

    public static Product mapToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setProductId(productDto.getId());
        product.setNameProduct(productDto.getName());
        product.setDescriptionProduct(productDto.getDescription());
        product.setPrice(productDto.getPrice());

        if (productDto.getGroup_id() != null) {
            Group group = new Group();
            group.setGroupId(productDto.getGroup_id());
            product.setGroup(group);
        }

        if (productDto.getCartDtoList() != null) {
            List<Cart> carts = productDto.getCartDtoList().stream()
                    .map(cartId -> {
                        Cart cart = new Cart();
                        cart.setCartId(cartId);
                        return cart;
                    })
                    .collect(Collectors.toList());
            product.setCartList(carts);
        }

        if (productDto.getOrderDtoList() != null) {
            List<Order> orders = productDto.getOrderDtoList().stream()
                    .map(orderId -> {
                        Order order = new Order();
                        order.setOrderId(orderId);
                        return order;
                    })
                    .collect(Collectors.toList());
            product.setOrderList(orders);
        }
        return product;
    }

    public static List<ProductDto> mapToProductDtoList(List<Product> productList) {
        return productList.stream()
                .map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
    }

    public static List<Product> mapToProductList(List<ProductDto> productDtoList) {
        return productDtoList.stream()
                .map(ProductMapper::mapToProduct)
                .collect(Collectors.toList());
    }
}
