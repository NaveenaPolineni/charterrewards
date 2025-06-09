package com.charter.rewards.charterrewards.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.charter.rewards.charterrewards.dto.RetailTransaction;
import com.charter.rewards.charterrewards.exception.TransactionNotFoundException;
import com.charter.rewards.charterrewards.model.Transaction;
import com.charter.rewards.charterrewards.repository.TransactionRepository;
import com.charter.rewards.charterrewards.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith({ MockitoExtension.class })

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private RetailTransaction retailTransaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction(1L, 100.0, LocalDate.of(2024, 6, 1));
        transaction.setId(10L);

        retailTransaction = new RetailTransaction(10L, 1L, LocalDate.of(2024, 6, 1), 100.0);
    }

    @Test
    @DisplayName("Get Transaction By CustomerId returns Transaction Successfully")
    void testGetTransactionsByCustomerIdReturnsTransactions() {
        when(transactionRepository.findByCustomerId(1L)).thenReturn(Arrays.asList(transaction));

        List<RetailTransaction> result = transactionService.getTransactionsByCustomerId(1L);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getTransactionId());
        assertEquals(1L, result.get(0).getCustomerId());
        assertEquals(100.0, result.get(0).getAmount());
    }

    @Test
    @DisplayName("Get Transaction By CustomerId throws TransactionNotFoundException when no transactions found")
    void testGetTransactionsByCustomerIdThrowsExceptionWhenEmpty() {
        when(transactionRepository.findByCustomerId(2L)).thenReturn(Collections.emptyList());

        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.getTransactionsByCustomerId(2L);
        });
    }

    @Test
    @DisplayName("Create Transaction returns Transaction Successfully")
    void testCreateTransactionWithPurchaseDate() {
        RetailTransaction request = new RetailTransaction(null, 1L, LocalDate.of(2024, 6, 2), 200.0);
        Transaction saved = new Transaction(1L, 200.0, LocalDate.of(2024, 6, 2));
        saved.setId(20L);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);

        RetailTransaction result = transactionService.createTransaction(request);

        assertEquals(20L, result.getTransactionId());
        assertEquals(1L, result.getCustomerId());
        assertEquals(200.0, result.getAmount());
        assertEquals(LocalDate.of(2024, 6, 2), result.getPurchaseDate());
    }

    @Test
    @DisplayName("Create Transaction without Purchase Date uses current date")
    void testCreateTransactionWithoutPurchaseDate() {
        RetailTransaction request = new RetailTransaction(null, 1L, null, 150.0);
        Transaction saved = new Transaction(1L, 150.0, LocalDate.now());
        saved.setId(30L);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);

        RetailTransaction result = transactionService.createTransaction(request);

        assertEquals(30L, result.getTransactionId());
        assertEquals(1L, result.getCustomerId());
        assertEquals(150.0, result.getAmount());
        assertEquals(LocalDate.now(), result.getPurchaseDate());
    }

    @Test
    @DisplayName("Delete Transaction By Id deletes transaction successfully")
    void testDeleteTransactionByIdSuccess() {
        when(transactionRepository.findById(10L)).thenReturn(Optional.of(transaction));

        assertDoesNotThrow(() -> transactionService.deleteTransactionById(10L));
        verify(transactionRepository, times(1)).delete(transaction);
    }

    @Test
    @DisplayName("Delete Transaction By Id throws TransactionNotFoundException when transaction does not exist")
    void testDeleteTransactionByIdNotFound() {
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.deleteTransactionById(99L);
        });
    }
}
