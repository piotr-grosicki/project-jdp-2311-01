package com.kodilla.ecommercee.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "CARTS")
public class Cart {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "ID", unique = true)
    private Long cartId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "CREATION_DATE")
    private LocalDate creationDate;

    @Column(name = "ACTIVE")
    private boolean active;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "PRODUCT_LIST",
            joinColumns = @JoinColumn(name = "CART_ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID")
    )
    private List<Product> productsList = new ArrayList<>();
}