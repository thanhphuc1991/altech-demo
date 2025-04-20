package com.altech.demo.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Product {
    private UUID id;
    private String name;
    private BigDecimal price;

    public Product(String name, BigDecimal price) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
    }

    // Getters & Setters
}

