package com.kodilla.ecommercee.domain;

import lombok.Data;

import java.util.List;

@Data
public class GroupDto {
        private Long groupId;
        private String name;
        private String description;
        private List<Product> productdDtoList;
}
