package com.kodilla.ecommercee.controller;

import com.kodilla.ecommercee.domain.CartDto;
import com.kodilla.ecommercee.domain.GenericEntity;
import com.kodilla.ecommercee.domain.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/v1/cart/")
public class CartController {

    // Dane
    private List<CartDto> carts;

    {
        carts = new ArrayList<>();

        // For simplicity, let's assume ProductDto has a constructor taking all fields
        ProductDto product1 = new ProductDto(1L, "Product A", "Description A", 10.0, 5L);
        ProductDto product2 = new ProductDto(2L, "Product B", "Description B", 20.0, 5L);
        List<ProductDto> productsForCart1 = new ArrayList<>(Arrays.asList(product1, product2));

        CartDto cart1 = new CartDto(1L, 101L, productsForCart1, true, LocalDateTime.now());

        List<ProductDto> productsForCart2 = new ArrayList<>(Collections.singletonList(product1));

        CartDto cart2 = new CartDto(2L, 102L, productsForCart2, false, LocalDateTime.now().minusDays(1));

        carts.add(cart1);
        carts.add(cart2);
    }



    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> createCart(@PathVariable Long userId) {
        long newCartId = carts.size() + 1L; // Generating a new unique cartId
        LocalDateTime currentDate = LocalDateTime.now();  // Getting the current date and time
        CartDto cart = new CartDto(newCartId, userId, new ArrayList<>(), true, currentDate);
        carts.add(cart);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable Long cartId) {
        CartDto cart = findCartById(cartId);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //dane
    private CartDto findCartById(Long cartId) {
        return carts.stream()
                .filter(cart -> cart.getCartId() == cartId)
                .findFirst()
                .orElse(null);
    }
    @PostMapping("/{cartId}/product")
    public ResponseEntity<CartDto> addProductToCart(@PathVariable Long cartId, @RequestBody ProductDto product) {
        CartDto cart = findCartById(cartId);
        if (cart != null) {
            cart.getProducts().add(product);
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{cartId}/product/{productId}")
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        CartDto cart = findCartById(cartId);
        if (cart != null) {
            cart.getProducts().removeIf(product -> product.getId() == productId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{cartId}/status")
    public ResponseEntity<CartDto> updateCartStatus(@PathVariable Long cartId, @RequestParam boolean status) {
        CartDto cart = findCartById(cartId);
        if (cart != null) {
            cart.setActive(status);
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        boolean removed = carts.removeIf(cart -> cart.getCartId().equals(cartId));
        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{cartId}/order")
    public ResponseEntity<GenericEntity> createOrderFromCart(@PathVariable Long cartId) {
        CartDto cart = findCartById(cartId);
        if (cart != null) {

            GenericEntity newOrder = new GenericEntity(cart.getCartId(), "Zamówienie nr " + cart.getCartId());

            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
