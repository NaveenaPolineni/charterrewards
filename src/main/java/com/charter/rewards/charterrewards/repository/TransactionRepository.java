package com.charter.rewards.charterrewards.repository;

import com.charter.rewards.charterrewards.model.Transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomerIdAndTransactionDateBetween(
            Long customerId,
            LocalDate startDate,
            LocalDate endDate);

    List<Transaction> findByCustomerId(Long customerId);

}
