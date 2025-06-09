package com.charter.rewards.charterrewards.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.charter.rewards.charterrewards.dto.Purchase;
import com.charter.rewards.charterrewards.model.Transaction;

@Component
public class PurchaseMapper {

    public List<Purchase> toPurchases(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return Collections.emptyList();
        }

        return transactions.stream()
                .map(transaction -> new Purchase(transaction.getTransactionDate(), transaction.getAmount()))
                .collect(Collectors.toList());
    }
}
