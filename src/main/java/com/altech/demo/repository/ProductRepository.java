package com.altech.demo.repository;

import com.altech.demo.model.Product;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductRepository {
    private final Map<UUID, Product> products = new HashMap<>();

    public Product save(Product product) {
        products.put(product.getId(), product);
        return product;
    }

    public Optional<Product> findById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }

    public void delete(UUID id) {
        products.remove(id);
    }

    public Collection<Product> findAll() {
        return products.values();
    }
}

