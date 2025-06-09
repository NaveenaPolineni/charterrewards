package com.charter.rewards.charterrewards.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.charter.rewards.charterrewards.repository.CustomerRepository;
import com.charter.rewards.charterrewards.repository.TransactionRepository;
import com.charter.rewards.charterrewards.service.RewardsService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.*;
import com.charter.rewards.charterrewards.dto.CustomerRewards;
import com.charter.rewards.charterrewards.exception.CustomerNotFoundException;
import com.charter.rewards.charterrewards.exception.InvalidArgumentsException;
import com.charter.rewards.charterrewards.model.Customer;
import com.charter.rewards.charterrewards.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@ExtendWith({ MockitoExtension.class })
public class RewardsServiceTest {

    @InjectMocks
    private RewardsService rewardsService;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TransactionRepository transactionRepository;

    private Customer customer;
    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        customer = new Customer("Naveena", "Polineni", "naveenam2203@gmail.com");
        customer.setId(1L);

        Transaction firsTransaction = new Transaction(1L, 120.0, LocalDate.now().minusMonths(1));
        Transaction secondTransaction = new Transaction(1L, 80.0, LocalDate.now().minusMonths(2));
        firsTransaction.setId(1L);
        secondTransaction.setId(2L);

        transactions = Arrays.asList(firsTransaction, secondTransaction);
    }

    @Test
    @DisplayName("Test calculatePoints method")
    void testCalculatePoints() {
        assertEquals(90, rewardsService.calculatePoints(120.49));
        assertEquals(30, rewardsService.calculatePoints(80.35));
        assertEquals(0, rewardsService.calculatePoints(45.0));
        assertEquals(51, rewardsService.calculatePoints(100.50));
    }

    @Test
    @DisplayName("Get rewards for customer without dates range returns rewards successfully")
    void testGetRewardsForCustomerReturnsRewardsSuccessfully() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(eq(1L), any(), any()))
                .thenReturn(transactions);

        CustomerRewards rewards = rewardsService.getRewardsForCustomer(1L, null, null);

        assertNotNull(rewards);
        assertEquals("Naveena", rewards.getFirstName());
        assertEquals("Polineni", rewards.getLastName());
        assertEquals(120, rewards.getTotalRewardsPoints());
        assertFalse(rewards.getMonthlyTransactions().isEmpty());
    }

    @Test
    @DisplayName("Get rewards for customer with specific date range returns rewards successfully")
    void testGetRewardsForCustomerWithDateRangeReturnsRewardsSuccessfully() {
        LocalDate startDate = LocalDate.now().minusMonths(2);
        LocalDate endDate = LocalDate.now();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(eq(1L), eq(startDate), eq(endDate)))
                .thenReturn(transactions);

        CustomerRewards rewards = rewardsService.getRewardsForCustomer(1L, startDate, endDate);

        assertNotNull(rewards);
        assertEquals("Naveena", rewards.getFirstName());
        assertEquals("Polineni", rewards.getLastName());
        assertEquals(120, rewards.getTotalRewardsPoints());
        assertFalse(rewards.getMonthlyTransactions().isEmpty());
    }

    @Test
    @DisplayName("Get rewards for customer with invalid date range throws InvalidArgumentsException")
    void testGetRewardsForCustomerWithInvalidDateRangeThrowsException() {
        LocalDate startDate = LocalDate.now().plusDays(1); // Future date
        LocalDate endDate = LocalDate.now();

        assertThrows(InvalidArgumentsException.class, () -> {
            rewardsService.getRewardsForCustomer(1L, startDate, endDate);
        });
    }

    @Test
    @DisplayName("Get rewards for customer returns CustomerNotFoundException when customer does not exist")
    void testGetRewardsForCustomerReturnsCustomerNotFound() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> {
            rewardsService.getRewardsForCustomer(2L, null, null);
        });
    }

    @Test
    @DisplayName("Get rewards for customer returns InvalidArgumentsException when no transactions found")
    void testGetRewardsForCustomerReturnsNoTransactions() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(eq(1L), any(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(InvalidArgumentsException.class, () -> {
            rewardsService.getRewardsForCustomer(1L, null, null);
        });
    }
}
