package com.kodilla.ecommercee.domain;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity(name = "carts")
public class Cart {

    @Id
    private Long id;

    // Relacja do User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
