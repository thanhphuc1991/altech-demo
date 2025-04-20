package com.altech.demo.controller;

import com.altech.demo.model.Deal;
import com.altech.demo.model.Product;
import com.altech.demo.repository.DealRepository;
import com.altech.demo.repository.ProductRepository;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@Api(value = "Admin Operations", description = "Operations for admin users like managing products and deals.")
public class AdminController {

    private final ProductRepository productRepository;
    private final DealRepository dealRepository;

    public AdminController(ProductRepository productRepository, DealRepository dealRepository) {
        this.productRepository = productRepository;
        this.dealRepository = dealRepository;
    }

    // --- Create Product ---
    @ApiOperation(value = "Create a new product", notes = "Add a new product to the store")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Product created successfully"),
            @ApiResponse(code = 400, message = "Invalid input")
    })
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(
            @RequestParam String name,
            @RequestParam BigDecimal price) {

        Product product = new Product(name, price);
        productRepository.save(product);
        return ResponseEntity.ok(product);
    }

    // --- Remove Product ---
    @ApiOperation(value = "Delete a product", notes = "Remove a product from the store")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product deleted successfully"),
            @ApiResponse(code = 404, message = "Product not found")
    })
    @DeleteMapping("/products")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productRepository.delete(id);
        dealRepository.deleteByProductId(id); // Remove associated deal if any
        return ResponseEntity.noContent().build();
    }

    // --- Create Discount Deal ---
    @ApiOperation(value = "Create a discount deal", notes = "Add a discount deal to a product")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Deal created successfully"),
            @ApiResponse(code = 400, message = "Invalid deal data")
    })
    @PostMapping("/deals")
    public ResponseEntity<Deal> createDeal(
            @RequestParam UUID productId,
            @RequestParam int buyQty,
            @RequestParam int discountQty,
            @RequestParam int percent) {

        Deal deal = new Deal(productId, buyQty, discountQty, percent);
        dealRepository.save(deal);
        return ResponseEntity.ok(deal);
    }
}
