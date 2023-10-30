package com.kodilla.ecommercee.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CartDto {
    private Long cartId;
    private Long userId;
    private List<ProductDto> products;
    private boolean active;
    private LocalDateTime creationDate;
}
