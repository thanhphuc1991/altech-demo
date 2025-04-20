package com.altech.demo;

import com.altech.demo.model.Deal;
import com.altech.demo.model.Product;
import com.altech.demo.repository.BasketRepository;
import com.altech.demo.repository.DealRepository;
import com.altech.demo.repository.ProductRepository;
import com.altech.demo.service.BasketService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductRepositoryTest {

    @Test
    public void testSaveAndFindProduct() {
        ProductRepository repo = new ProductRepository();
        Product product = new Product("Laptop", BigDecimal.valueOf(1200));
        repo.save(product);

        assertTrue(repo.findById(product.getId()).isPresent());
        assertEquals("Laptop", repo.findById(product.getId()).get().getName());
    }

    @Test
    public void testDeleteProduct() {
        ProductRepository repo = new ProductRepository();
        Product product = new Product("Mouse", BigDecimal.valueOf(20));
        repo.save(product);
        repo.delete(product.getId());

        assertFalse(repo.findById(product.getId()).isPresent());
    }

    @Test
    public void testBasketAddProductAndReceipt() {
        ProductRepository productRepo = new ProductRepository();
        BasketRepository basketRepo = new BasketRepository();
        DealRepository dealRepo = new DealRepository();
        BasketService basketService = new BasketService(productRepo, basketRepo, dealRepo);

        Product product = new Product("TV", BigDecimal.valueOf(1000));
        productRepo.save(product);

        UUID basketId = basketService.createBasket();
        basketService.addProduct(basketId, product.getId());
        basketService.addProduct(basketId, product.getId());

        Deal deal = new Deal(product.getId(), 1, 1, 50);
        dealRepo.save(deal);

        String receipt = basketService.generateReceipt(basketId);
        assertTrue(receipt.contains("TV"));
        assertTrue(receipt.contains("Total:"));
    }

    @Test
    public void testRemoveProductFromBasket() {
        ProductRepository productRepo = new ProductRepository();
        BasketRepository basketRepo = new BasketRepository();
        DealRepository dealRepo = new DealRepository();
        BasketService basketService = new BasketService(productRepo, basketRepo, dealRepo);

        Product product = new Product("Phone", BigDecimal.valueOf(800));
        productRepo.save(product);

        UUID basketId = basketService.createBasket();
        basketService.addProduct(basketId, product.getId());
        basketService.removeProduct(basketId, product.getId());

        String receipt = basketService.generateReceipt(basketId);
        assertTrue(receipt.contains("Total: 0"));
    }

    @Test
    public void testEmptyBasketReceipt() {
        ProductRepository productRepo = new ProductRepository();
        BasketRepository basketRepo = new BasketRepository();
        DealRepository dealRepo = new DealRepository();
        BasketService basketService = new BasketService(productRepo, basketRepo, dealRepo);

        UUID basketId = basketService.createBasket();
        String receipt = basketService.generateReceipt(basketId);
        assertTrue(receipt.contains("Total: 0"));
    }

    @Test
    public void testDealWithNoRemainder() {
        ProductRepository productRepo = new ProductRepository();
        BasketRepository basketRepo = new BasketRepository();
        DealRepository dealRepo = new DealRepository();
        BasketService basketService = new BasketService(productRepo, basketRepo, dealRepo);

        Product product = new Product("Camera", BigDecimal.valueOf(100));
        productRepo.save(product);

        Deal deal = new Deal(product.getId(), 1, 1, 50);
        dealRepo.save(deal);

        UUID basketId = basketService.createBasket();
        basketService.addProduct(basketId, product.getId());
        basketService.addProduct(basketId, product.getId());

        String receipt = basketService.generateReceipt(basketId);
        assertTrue(receipt.contains("Camera"));
        assertTrue(receipt.contains("Total:"));
    }

    @Test
    public void testNonExistentBasketThrows() {
        BasketRepository basketRepo = new BasketRepository();
        ProductRepository productRepo = new ProductRepository();
        DealRepository dealRepo = new DealRepository();
        BasketService basketService = new BasketService(productRepo, basketRepo, dealRepo);

        UUID randomId = UUID.randomUUID();
        Exception exception = assertThrows(RuntimeException.class, () -> {
            basketService.generateReceipt(randomId);
        });

        assertEquals("Basket not found", exception.getMessage());
    }

    @Test
    public void testRemoveNonExistentProductDoesNotCrash() {
        ProductRepository productRepo = new ProductRepository();
        BasketRepository basketRepo = new BasketRepository();
        DealRepository dealRepo = new DealRepository();
        BasketService basketService = new BasketService(productRepo, basketRepo, dealRepo);

        UUID basketId = basketService.createBasket();
        UUID fakeProductId = UUID.randomUUID();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            basketService.removeProduct(basketId, fakeProductId);
        });

        assertEquals("Product not found", exception.getMessage());
    }
}
