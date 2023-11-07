package com.kodilla.ecommercee.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GroupDto {

        private Long groupId;
        private String name;
        private String description;
        private List<ProductDto> productDtoList;
}
