package com.charter.rewards.charterrewards.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.charter.rewards.charterrewards.dto.RetailTransaction;
import com.charter.rewards.charterrewards.exception.TransactionNotFoundException;
import com.charter.rewards.charterrewards.model.Transaction;
import com.charter.rewards.charterrewards.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<RetailTransaction> getTransactionsByCustomerId(Long customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found for customer ID: " + customerId);
        }
        return transactions.stream()
                .map(transaction -> new RetailTransaction(
                        transaction.getId(),
                        transaction.getCustomerId(),
                        transaction.getTransactionDate(),
                        transaction.getAmount()))
                .collect(Collectors.toList());
    }

    public RetailTransaction createTransaction(RetailTransaction transactionRequest) {

        Transaction transaction = new Transaction(
                transactionRequest.getCustomerId(),
                transactionRequest.getAmount(),
                null != transactionRequest.getPurchaseDate() ? transactionRequest.getPurchaseDate()
                        : java.time.LocalDate.now());

        Transaction createdTransaction = transactionRepository.save(transaction);

        return new RetailTransaction(
                createdTransaction.getId(),
                createdTransaction.getCustomerId(),
                createdTransaction.getTransactionDate(),
                createdTransaction.getAmount());
    }

    public void deleteTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + id));
        transactionRepository.delete(transaction);
    }

}
