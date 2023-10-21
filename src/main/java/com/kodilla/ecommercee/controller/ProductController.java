package com.kodilla.ecommercee.controller;

import com.kodilla.ecommercee.domain.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    public ProductController() {

        products = new ArrayList<>();
        products.add(new ProductDto(1, "Product 1", "Description 1", 10.0));
        products.add(new ProductDto(2, "Product 2", "Description 2", 15.0));
    }

    private List<ProductDto> products = new ArrayList<>();
    private long nextProductId = 1;

    @GetMapping
    public List<ProductDto> getAllProducts() {

        return products;
    }

    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable Long productId) {

        return findProductById(productId);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        productDto.setId(nextProductId);
        products.add(productDto);
        nextProductId++;
        return new ResponseEntity<ProductDto>(productDto, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ProductDto updateProduct(@PathVariable Long productId, @RequestBody ProductDto updatedProduct) {

        ProductDto existingProduct = findProductById(productId);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        return existingProduct;
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        // Usunięcie produktu o określonym ID (dane przykładowe)
        ProductDto productToDelete = findProductById(productId);
        products.remove(productToDelete);
    }

    private ProductDto findProductById(Long productId) {
        for (ProductDto product : products) {
            if (product.getId()==(productId.longValue())) {
                return product;
            }
        }
        throw new IllegalArgumentException("Product not found");
    }
}


