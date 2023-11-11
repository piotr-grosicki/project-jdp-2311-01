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
    @Column(name = "CART_ID", unique = true)
    private Long cartId;

    @Column(name = "CREATION_DATE")
    private LocalDate creationDate;

    @Column(name = "ACTIVE")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "JOIN_CART_PRODUCT",
            joinColumns = {@JoinColumn(name = "CART_ID", referencedColumnName = "CART_ID")},
            inverseJoinColumns = {@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")}
    )
    private List<Product> productsList = new ArrayList<>();

    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL)
    private Order order;
}