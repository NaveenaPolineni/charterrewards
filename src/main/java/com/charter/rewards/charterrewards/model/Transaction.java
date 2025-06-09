package com.charter.rewards.charterrewards.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Customer ID is required.")
    private Long customerId;
    @NotNull(message = "Purchase Amount is required.")
    private Double amount;
    @NotNull(message = "Transaction Date is required.")
    private LocalDate transactionDate;

    public Transaction() {
    }

    public Transaction(Long customerId, Double amount, LocalDate date) {
        this.customerId = customerId;
        this.amount = amount;
        this.transactionDate = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

}
