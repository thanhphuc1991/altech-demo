package com.altech.demo.repository;

import com.altech.demo.model.Basket;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BasketRepository {
    private final Map<UUID, Basket> storage = new ConcurrentHashMap<>();

    public void save(Basket basket) {
        storage.put(basket.getId(), basket);
    }

    public Optional<Basket> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public void delete(UUID id) {
        storage.remove(id);
    }

    public List<Basket> findAll() {
        return new ArrayList<>(storage.values());
    }
}

