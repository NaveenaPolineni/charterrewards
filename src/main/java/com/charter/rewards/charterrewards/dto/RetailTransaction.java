package com.charter.rewards.charterrewards.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class RetailTransaction {
    private Long transactionId;
    private LocalDate purchaseDate;
    @NotNull(message = "Customer Id cannot be null")
    private Long customerId;
    @NotNull(message = "Amount cannot be null")
    private Double amount;

    public RetailTransaction(Long transactionId, Long customerId, LocalDate purchaseDate, Double amount) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.purchaseDate = purchaseDate;
        this.amount = amount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public Double getAmount() {
        return amount;
    }
}
