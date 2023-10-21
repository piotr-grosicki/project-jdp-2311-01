package com.kodilla.ecommercee.controller;

import com.kodilla.ecommercee.domain.ProductDto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductControllerTest {
    private ProductController productController = new ProductController();

    @Test
    public void testGetProductById() {
        // Given
        ProductDto sampleProduct = new ProductDto(1L, "Example Product", "Sample Description", 10.0);
        // When
        ProductDto result = productController.getProductById(1L);
        // Then
        assertEquals(sampleProduct.getId(), result.getId());
    }
}
