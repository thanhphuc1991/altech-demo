package com.altech.demo.service;

import com.altech.demo.model.Basket;
import com.altech.demo.model.Deal;
import com.altech.demo.model.Product;
import com.altech.demo.repository.BasketRepository;
import com.altech.demo.repository.DealRepository;
import com.altech.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class BasketService {
    private final ProductRepository productRepo;
    private final BasketRepository basketRepo;
    private final DealRepository dealRepo;

    public BasketService(ProductRepository productRepo, BasketRepository basketRepo, DealRepository dealRepo) {
        this.productRepo = productRepo;
        this.basketRepo = basketRepo;
        this.dealRepo = dealRepo;
    }

    public UUID createBasket() {
        Basket basket = new Basket();
        basketRepo.save(basket);
        return basket.getId();
    }

    public void addProduct(UUID basketId, UUID productId) {
        Basket basket = getBasketOrThrow(basketId);
        Product product = getProductOrThrow(productId);
        basket.addProduct(product);
    }

    public void removeProduct(UUID basketId, UUID productId) {
        Basket basket = getBasketOrThrow(basketId);
        Product product = getProductOrThrow(productId);
        basket.removeProduct(product);
    }

    public String generateReceipt(UUID basketId) {
        Basket basket = getBasketOrThrow(basketId);
        Map<Product, Integer> itemCounts = basket.getItemCounts();

        BigDecimal total = BigDecimal.ZERO;
        StringBuilder receipt = new StringBuilder("Receipt:\n");

        for (Map.Entry<Product, Integer> entry : itemCounts.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            BigDecimal price = product.getPrice();

            Deal deal = dealRepo.findByProductId(product.getId());
            BigDecimal lineTotal;

            if (deal != null) {
                int sets = quantity / (deal.getBuyQty() + deal.getDiscountQty());
                int remainder = quantity % (deal.getBuyQty() + deal.getDiscountQty());

                lineTotal = price.multiply(BigDecimal.valueOf(sets * deal.getBuyQty()));
                lineTotal = lineTotal.add(
                        price.multiply(BigDecimal.valueOf(sets * deal.getDiscountQty()))
                                .multiply(BigDecimal.valueOf((100 - deal.getPercent()) / 100.0))
                );
                lineTotal = lineTotal.add(price.multiply(BigDecimal.valueOf(remainder)));
            } else {
                lineTotal = price.multiply(BigDecimal.valueOf(quantity));
            }

            receipt.append(product.getName())
                    .append(" x ").append(quantity)
                    .append(" = ").append(lineTotal).append("\n");

            total = total.add(lineTotal);
        }

        receipt.append("Total: ").append(total);
        return receipt.toString();
    }

    private Basket getBasketOrThrow(UUID id) {
        return basketRepo.findById(id).orElseThrow(() -> new RuntimeException("Basket not found"));
    }

    private Product getProductOrThrow(UUID id) {
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
