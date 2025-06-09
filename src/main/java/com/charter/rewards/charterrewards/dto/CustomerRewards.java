package com.charter.rewards.charterrewards.dto;

import java.util.List;

public class CustomerRewards {      
    
    private Long customerId;
    private String firstName;
    private String lastName;
    private int totalRewardsPoints;
    private List<MonthlyTransactions> monthlyTransactions;

    public CustomerRewards(Long customerId, String firstName, String lastName, int totalRewardsPoints, List<MonthlyTransactions> monthlyTransactions) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalRewardsPoints = totalRewardsPoints;
        this.monthlyTransactions = monthlyTransactions;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getTotalRewardsPoints() {
        return totalRewardsPoints;
    }

    public List<MonthlyTransactions> getMonthlyTransactions() {
        return monthlyTransactions;
    }
    
}
