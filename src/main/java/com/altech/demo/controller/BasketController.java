package com.altech.demo.controller;

import com.altech.demo.service.BasketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/basket")
@Api(value = "Basket Operations", description = "Operations for managing the customer basket and checkout.")
public class BasketController {

    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    // --- Create new basket ---
    @ApiOperation(value = "Create a new basket", notes = "Create a new empty basket for the customer.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Basket created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<UUID> createBasket() {
        UUID basketId = basketService.createBasket();
        return ResponseEntity.ok(basketId);
    }

    // --- Add product to basket ---
    @ApiOperation(value = "Add a product to the basket", notes = "Add a product by product ID to the customer's basket.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product added to basket successfully"),
            @ApiResponse(code = 404, message = "Product or basket not found")
    })
    @PostMapping("/{basketId}/add")
    public ResponseEntity<Void> addProduct(
            @PathVariable UUID basketId,
            @RequestParam UUID productId) {
        basketService.addProduct(basketId, productId);
        return ResponseEntity.ok().build();
    }

    // --- Remove product from basket ---
    @ApiOperation(value = "Remove a product from the basket", notes = "Remove a product by product ID from the customer's basket.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product removed from basket successfully"),
            @ApiResponse(code = 404, message = "Product or basket not found")
    })
    @DeleteMapping("/{basketId}/remove")
    public ResponseEntity<Void> removeProduct(
            @PathVariable UUID basketId,
            @RequestParam UUID productId) {
        basketService.removeProduct(basketId, productId);
        return ResponseEntity.ok().build();
    }

    // --- Get receipt for basket ---
    @ApiOperation(value = "Get basket receipt", notes = "Generate a receipt for the basket including products, discounts, and total price.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Receipt generated successfully"),
            @ApiResponse(code = 404, message = "Basket not found")
    })
    @GetMapping("/{basketId}/receipt")
    public ResponseEntity<String> getReceipt(@PathVariable UUID basketId) {
        String receipt = basketService.generateReceipt(basketId);
        return ResponseEntity.ok(receipt);
    }
}
