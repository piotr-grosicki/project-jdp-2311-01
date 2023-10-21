package com.kodilla.ecommercee.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class ProductDto {
    private long id;
    private String name;
    private String description;
    private double price;

}
