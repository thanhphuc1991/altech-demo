package com.altech.demo.repository;

import com.altech.demo.model.Deal;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DealRepository {
    private final Map<UUID, Deal> dealStorage = new ConcurrentHashMap<>();

    public void save(Deal deal) {
        dealStorage.put(deal.getProductId(), deal);
    }

    public Optional<Deal> findOptionalByProductId(UUID productId) {
        return Optional.ofNullable(dealStorage.get(productId));
    }

    public Deal findByProductId(UUID productId) {
        return dealStorage.get(productId);
    }

    public void deleteByProductId(UUID productId) {
        dealStorage.remove(productId);
    }

    public void clear() {
        dealStorage.clear();
    }
}
