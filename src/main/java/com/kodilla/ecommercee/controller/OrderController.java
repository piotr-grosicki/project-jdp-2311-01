package com.kodilla.ecommercee.controller;

import com.kodilla.ecommercee.domain.GenericEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    @GetMapping
    public List<GenericEntity>  getOrders() {
        List<GenericEntity> orders = new ArrayList<>();
        orders.add(new GenericEntity(1L, "Order 1"));
        orders.add(new GenericEntity(2L, "Order 2"));
        orders.add(new GenericEntity(3L, "Order 3"));
        return orders;
    }

    @GetMapping(value = "/{id}")
    public GenericEntity getOrder(@PathVariable Long id) {
        return new GenericEntity(1L, "Test order");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createOrder(@RequestBody GenericEntity genericEntity) {
    }

    @PutMapping(value = "/{id}")
    public GenericEntity updateOrder(@PathVariable Long id, @RequestBody GenericEntity genericEntity) {
        return new GenericEntity(8L, "Edited test order");
    }

    @DeleteMapping(value = "/{id}")
    public void deleteOrder(@PathVariable Long id) {
    }
}