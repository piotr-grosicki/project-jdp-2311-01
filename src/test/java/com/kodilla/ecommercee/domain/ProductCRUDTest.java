package com.kodilla.ecommercee.domain;

import com.kodilla.ecommercee.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductCRUDTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DirtiesContext
    public void testCreateProduct(){
        //Given
        Product product = new Product();
        product.setNameProduct("testName");
        product.setDescriptionProduct("testDescription");

        //When
        productRepository.save(product);
        String productName = product.getNameProduct();
        Product productNotNull = productRepository.findById(product.getProductId()).orElse(null);

        //Then
        assertNotNull(productNotNull);
        assertEquals(productName, "testName");
    }

    @Test
    @DirtiesContext
    public void testFindProductById(){
        //Given
        Product product = new Product();
        product.setNameProduct("testName");
        product.setDescriptionProduct("testDescription");

        //When
        productRepository.save(product);
        Product foundProduct = productRepository.findById(product.getProductId()).orElse(null);

        //Then
        assertEquals(foundProduct.getProductId(), product.getProductId());
        assertNotNull(foundProduct);
    }

    @Test
    @DirtiesContext
    public void testUpdateProduct(){
        //Given
        Product product = new Product();
        product.setNameProduct("testName");
        product.setDescriptionProduct("testDescription");

        //When
        Product productBefore = productRepository.save(product);
        productBefore.setNameProduct("testName1");
        productBefore.setDescriptionProduct("testDescription1");
        productRepository.save(productBefore);
        Product productAfter = productRepository.findById(productBefore.getProductId()).orElse(null);

        //Then
        assertEquals("testName1", productAfter.getNameProduct());
        assertEquals("testDescription1", productAfter.getDescriptionProduct());
        assertNotNull(productAfter);
    }

    @Test
    @DirtiesContext
    public void testDeleteProduct(){
        //Given
        Product product = new Product();
        product.setNameProduct("testName");
        product.setDescriptionProduct("testDescription");

        //When
        Product addedProduct = productRepository.save(product);
        productRepository.deleteById(addedProduct.getProductId());
        Product deletedProduct = productRepository.findById(addedProduct.getProductId()).orElse(null);

        //Then
        assertNull(deletedProduct);
    }

}

