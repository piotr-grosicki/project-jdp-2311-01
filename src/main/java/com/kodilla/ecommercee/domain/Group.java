package com.kodilla.ecommercee.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Group {
    @Id
    private Long id;
}
