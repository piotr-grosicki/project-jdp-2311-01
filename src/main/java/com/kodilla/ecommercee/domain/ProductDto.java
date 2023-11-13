package com.kodilla.ecommercee.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long group_id;
    private List<Long> cartDtoList;
    private List<Long> orderDtoList;

    public ProductDto(Long id, String name, String description, Double price, Long group_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.group_id = group_id;
    }
}
