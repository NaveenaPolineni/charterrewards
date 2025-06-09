package com.charter.rewards.charterrewards.dto;

import java.time.LocalDate;

public class Purchase {
    private LocalDate purchaseDate;;
    private Double amount;

    public Purchase(LocalDate purchaseDate, Double amount) {
        this.purchaseDate = purchaseDate;
        this.amount = amount;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }


    public Double getAmount() {
        return amount;
    }
}
