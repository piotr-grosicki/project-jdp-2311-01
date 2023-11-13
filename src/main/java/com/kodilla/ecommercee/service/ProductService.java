package com.kodilla.ecommercee.service;

import com.kodilla.ecommercee.domain.Product;
import com.kodilla.ecommercee.domain.ProductDto;
import com.kodilla.ecommercee.exceptions.ProductAlreadyExistsException;
import com.kodilla.ecommercee.exceptions.ProductNotFoundException;
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
        return ProductMapper.mapToProductDtoList(products);
    }

    public ProductDto getProductById(Long productId) throws ProductNotFoundException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return ProductMapper.mapToProductDto(product);
        } else {
            throw new ProductNotFoundException();
        }
    }

    public ProductDto createProduct(ProductDto productDto) throws ProductAlreadyExistsException {
        if (productDto.getId() != 0) {
            throw new ProductAlreadyExistsException();
        }

        Product product = ProductMapper.mapToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.mapToProductDto(savedProduct);
    }

    public ProductDto updateProduct(Long productId, ProductDto updatedProduct) throws ProductNotFoundException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product existingProduct = productOptional.get();
            existingProduct.setNameProduct(updatedProduct.getName());
            existingProduct.setDescriptionProduct(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            Product updatedProductEntity = productRepository.save(existingProduct);
            return ProductMapper.mapToProductDto(updatedProductEntity);
        } else {
            throw new ProductNotFoundException();
        }
    }

    public void deleteProduct(Long productId) throws ProductNotFoundException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            productRepository.deleteById(productId);
        } else {
            throw new ProductNotFoundException();
        }
    }
}
