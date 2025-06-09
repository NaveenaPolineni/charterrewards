package com.charter.rewards.charterrewards.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.charter.rewards.charterrewards.dto.CustomerRewards;
import com.charter.rewards.charterrewards.dto.MonthlyTransactions;
import com.charter.rewards.charterrewards.exception.CustomerNotFoundException;
import com.charter.rewards.charterrewards.exception.InvalidArgumentsException;
import com.charter.rewards.charterrewards.mapper.PurchaseMapper;
import com.charter.rewards.charterrewards.mapper.RewardsMapper;
import com.charter.rewards.charterrewards.repository.CustomerRepository;
import com.charter.rewards.charterrewards.repository.TransactionRepository;
import com.charter.rewards.charterrewards.model.Customer;
import com.charter.rewards.charterrewards.model.Transaction;

@Service
public class RewardsService {
    private static final Logger log = LoggerFactory.getLogger(RewardsService.class);

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final RewardsMapper rewardsMapper = new RewardsMapper();
    private final PurchaseMapper purchaseMapper = new PurchaseMapper();

    @Autowired
    public RewardsService(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public CustomerRewards getRewardsForCustomer(Long customerId, LocalDate starDate, LocalDate endDate) {

        validateDateRange(starDate, endDate);
        endDate = validateAndUpdateEndDate(starDate, endDate);
        starDate = validateAndUpdateStartDate(starDate, endDate);

        log.info("Fetching customer with ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer does not exist for the provided ID: " + customerId));

        log.info("Fetching transactions for customer ID: {} from {} to {}", customerId, starDate, endDate);
        List<Transaction> transactions = transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId,
                starDate, endDate);
        if (transactions.isEmpty()) {
            log.info("No transactions found for customer ID: {} between {} and {}", customerId, starDate, endDate);
            throw new InvalidArgumentsException("No transactions found for the provided date range.");
        }

        log.info("Calculating rewards for customer ID: {} from {} to {}", customerId, starDate, endDate);
        int totalPoints = transactions.stream()
                .mapToInt(transaction -> calculatePoints(transaction.getAmount()))
                .sum();
        log.info("Total points earned by customer ID {} from {} to {}: {}", customerId, starDate, endDate, totalPoints);

        Map<String, List<Transaction>> transactionsByMonth = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTransactionDate().getMonth().name()));

        List<MonthlyTransactions> monthlyRewardsList = transactionsByMonth.entrySet().stream()
                .map(entry -> {
                    String month = entry.getKey();
                    List<Transaction> monthTransactions = entry.getValue();
                    int points = monthTransactions.stream()
                            .mapToInt(t -> calculatePoints(t.getAmount()))
                            .sum();
                    return new MonthlyTransactions(month, points, purchaseMapper.toPurchases(monthTransactions));
                })
                .collect(Collectors.toList());

        return rewardsMapper.toCustomerRewards(customer, totalPoints, monthlyRewardsList);
    }

    public int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (int) ((amount - 100) * 2 + 50);
        } else if (amount > 50) {
            points += (int) (amount - 50);
        }
        return points;
    }

    private static void validateDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        if (startDate != null && startDate.isAfter(today)) {
            log.error("startDate {} is in the future.", startDate);
            throw new InvalidArgumentsException(
                    "Future dates are not allowed. startDate provided should be in the past or today.");
        }
        if (endDate != null && endDate.isAfter(today)) {
            log.error("endDate {} is in the future", endDate);
            throw new InvalidArgumentsException(
                    "Future dates are not allowed. startDate provided should be in the past or today.");
        }
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            log.error("startDate {} is after endDate {}.", startDate, endDate);
            throw new InvalidArgumentsException("Invalid Date Range Provided.");
        }
    }

    /**
     * Validates and updates the start date based on the provided end date.
     * If both dates are null, it defaults to 3 months ago.
     * If only the end date is provided, it defaults the start date to 3 months
     * before the end date.
     *
     * @param startDate The start date to validate and update.
     * @param endDate   The end date to validate against.
     * @return The validated and updated start date.
     */
    private static LocalDate validateAndUpdateStartDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            log.info("No startDate or endDate provided, defaulting to 3 months ago.");
            startDate = LocalDate.now().minusMonths(3);
        } else if (startDate == null && endDate != null) {
            startDate = endDate.minusMonths(3); // Default to one month ago if not provided
            log.info("No startDate provided, only endDate available, defaulting to 3 months prior to endDate");

        }
        return startDate;
    }

    /**
     * Validates and updates the end date based on the provided start date.
     * If both dates are null, it defaults to today's date.
     * If only the start date is provided, it defaults the end date to today's date.
     * This will get transactions from start date to current date.
     *
     * @param startDate The start date to validate and update.
     * @param endDate   The end date to validate against.
     * @return The validated and updated end date.
     */
    private static LocalDate validateAndUpdateEndDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            log.info("No endDate or startDate provided, defaulting to today's date");
            endDate = LocalDate.now();
        } else if (startDate != null && endDate == null) {
            endDate = LocalDate.now();
            log.info("No endDate provided, defaulting to today's date");
        }
        return endDate;
    }

}
