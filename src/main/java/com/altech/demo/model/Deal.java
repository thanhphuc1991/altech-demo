package com.altech.demo.model;

import lombok.Data;
import java.util.UUID;

@Data
public class Deal {
    private UUID productId;
    private int buyQty;
    private int discountQty;
    private int percent;

    public Deal(UUID productId, int buyQty, int discountQty, int percent) {
        this.productId = productId;
        this.buyQty = buyQty;
        this.discountQty = discountQty;
        this.percent = percent;
    }
}
