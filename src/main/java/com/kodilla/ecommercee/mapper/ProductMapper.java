package com.kodilla.ecommercee.mapper;

import com.kodilla.ecommercee.domain.Product;
import com.kodilla.ecommercee.domain.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductMapper {

    public Product mapToProduct(final ProductDto productDto) {
        Product product = new Product();
        product.setNameProduct(productDto.getName());
        product.setDescriptionProduct(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        return product;
    }

    public ProductDto mapToProductDto(final Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getProductId());
        productDto.setName(product.getNameProduct());
        productDto.setDescription(product.getDescriptionProduct());
        productDto.setPrice(product.getPrice());
        return productDto;
    }

    public List<ProductDto> mapToProductDtoList(final List<Product> productList) {
        return productList.stream()
                .map(this::mapToProductDto)
                .collect(Collectors.toList());
    }

    public List<Product> mapToProductList(final List<ProductDto> productDtoList) {
        return productDtoList.stream()
                .map(this::mapToProduct)
                .collect(Collectors.toList());
    }
}
