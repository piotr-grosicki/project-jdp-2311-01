package com.kodilla.ecommercee.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PRODUCTS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "NAME")
    private String nameProduct;

    @Column(name = "DESCRIPTION")
    private String descriptionProduct;

    @Column(name = "PRICE")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "productsList")
    private List<Cart> cartList = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "productList")
    private List<Order> orderList;
}