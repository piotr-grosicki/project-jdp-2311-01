package com.kodilla.ecommercee.domain;

import lombok.Data;
import javax.persistence.*;


@Entity
@Data
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
