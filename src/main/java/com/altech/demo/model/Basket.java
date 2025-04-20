package com.altech.demo.model;

import com.altech.demo.model.Product;
import lombok.Data;

import java.util.*;

@Data
public class Basket {
    private final UUID id;
    private final Map<Product, Integer> productQuantities;

    public Basket() {
        this.id = UUID.randomUUID();
        this.productQuantities = new HashMap<>();
    }

    public UUID getId() {
        return id;
    }

    public void addProduct(Product product) {
        productQuantities.merge(product, 1, Integer::sum);
    }

    public void removeProduct(Product product) {
        productQuantities.computeIfPresent(product, (k, v) -> (v > 1) ? v - 1 : null);
    }

    public Map<Product, Integer> getItemCounts() {
        return Collections.unmodifiableMap(productQuantities);
    }
}
