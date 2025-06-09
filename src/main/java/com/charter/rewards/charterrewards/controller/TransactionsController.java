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

    /**
     * Creates a new retail transaction.
     *
     * @param request the details of the transaction to create
     * @return ResponseEntity containing the created transaction and its location
     */
    @PostMapping("/api/v1/transactions")
    public ResponseEntity<RetailTransaction> createTransaction(@Valid @RequestBody RetailTransaction request) {

        RetailTransaction createdTransaction = transactionService.createTransaction(request);

        URI transactionResourceLocation = URI.create(String.format("/customers/%s/transactions/%s",
                createdTransaction.getCustomerId(), createdTransaction.getTransactionId()));
        return ResponseEntity.created(transactionResourceLocation).body(createdTransaction);

    }

    /**
     * Retrieves all transactions for a specific customer by their ID.
     *
     * @param customerId the ID of the customer whose transactions to retrieve
     * @return ResponseEntity containing a list of RetailTransaction objects
     */
    @GetMapping("/api/v1/customers/{customerId}/transactions")
    public ResponseEntity<List<RetailTransaction>> getTransactionsByCustomerId(@PathVariable Long customerId) {

        List<RetailTransaction> transactions = transactionService.getTransactionsByCustomerId(customerId);

        return ResponseEntity.ok(transactions);
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param transactionId the ID of the transaction to delete
     * @return ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping("/api/v1/transactions/{transactionId}")
    public ResponseEntity<String> deleteTransactionById(@PathVariable Long transactionId) {

        transactionService.deleteTransactionById(transactionId);

        return ResponseEntity.ok("Transaction with ID: " + transactionId + " deleted successfully");
    }

}
