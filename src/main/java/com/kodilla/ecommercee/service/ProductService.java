package com.kodilla.ecommercee.service;

import com.kodilla.ecommercee.domain.Product;
import com.kodilla.ecommercee.domain.ProductDto;
import com.kodilla.ecommercee.exception.ProductAlreadyExistsException;
import com.kodilla.ecommercee.exception.ProductNotFoundException;
import com.kodilla.ecommercee.mapper.ProductMapper;
import com.kodilla.ecommercee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.mapToProductDtoList(products);
    }

    public ProductDto getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return productMapper.mapToProductDto(product);
        } else {
            throw new ProductNotFoundException();
        }
    }

    public ProductDto createProduct(ProductDto productDto) {
        if (productDto.getId() != 0) {
            throw new ProductAlreadyExistsException();
        }

        Product product = productMapper.mapToProduct(productDto);
        productRepository.save(product);
        return productDto;
    }

    public ProductDto updateProduct(Long productId, ProductDto updatedProduct) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product existingProduct = productOptional.get();
            existingProduct.setNameProduct(updatedProduct.getName());
            existingProduct.setDescriptionProduct(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            Product updatedProductEntity = productRepository.save(existingProduct);
            return productMapper.mapToProductDto(updatedProductEntity);
        } else {
            throw new ProductNotFoundException();
        }
    }

    public void deleteProduct(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            productRepository.deleteById(productId);
        } else {
            throw new ProductNotFoundException();
        }
    }
}
