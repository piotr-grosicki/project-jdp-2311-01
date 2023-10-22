package com.kodilla.ecommercee.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "carts")
public class Cart {

    @Id
    private Long id;
}
