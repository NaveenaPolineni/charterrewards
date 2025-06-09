package com.charter.rewards.charterrewards.controller;

import org.springframework.web.bind.annotation.RestController;

import com.charter.rewards.charterrewards.dto.RetailTransaction;
import com.charter.rewards.charterrewards.service.TransactionService;

import jakarta.validation.Valid;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

@RestController
public class TransactionsController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/api/v1/transactions")
    public ResponseEntity<RetailTransaction> createTransaction(@Valid @RequestBody RetailTransaction request) {

        RetailTransaction createdTransaction = transactionService.createTransaction(request);

        URI transactionResourceLocation = URI.create(String.format("/customers/%s/transactions/%s",
                createdTransaction.getCustomerId(), createdTransaction.getTransactionId()));
        return ResponseEntity.created(transactionResourceLocation).body(createdTransaction);

    }

    @GetMapping("/api/v1/customers/{customerId}/transactions")
    public ResponseEntity<List<RetailTransaction>> getTransactionsByCustomerId(@PathVariable Long customerId) {

        List<RetailTransaction> transactions = transactionService.getTransactionsByCustomerId(customerId);

        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/api/v1/transactions/{transactionId}")
    public ResponseEntity<String> deleteTransactionById(@PathVariable Long transactionId) {

        transactionService.deleteTransactionById(transactionId);

        return ResponseEntity.ok("Transaction with ID: " + transactionId + " deleted successfully");
    }

}
