package com.charter.rewards.charterrewards.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.charter.rewards.charterrewards.dto.CustomerRewards;
import com.charter.rewards.charterrewards.dto.MonthlyTransactions;
import com.charter.rewards.charterrewards.model.Customer;

@Component
public class RewardsMapper {

    public CustomerRewards toCustomerRewards(Customer customer, int totalRewardsPoints,
            List<MonthlyTransactions> monthlyTransactions) {
        return new CustomerRewards(customer.getId(), customer.getFirstName(), customer.getLastName(),
                totalRewardsPoints, monthlyTransactions);
    }

}
