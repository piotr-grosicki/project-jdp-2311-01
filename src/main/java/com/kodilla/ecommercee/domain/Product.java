package com.kodilla.ecommercee.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name")
    private String nameProduct;

    @Column(name = "description")
    private String descriptionProduct;

    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}