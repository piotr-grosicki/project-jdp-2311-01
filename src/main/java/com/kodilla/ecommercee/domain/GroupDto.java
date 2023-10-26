package com.kodilla.ecommercee.domain;

import lombok.Data;

@Data
public class GroupDto {
        private Long groupId;
        private String name;
        private String description;
        private Product product;
}
