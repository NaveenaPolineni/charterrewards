package com.charter.rewards.charterrewards.dto;

import java.util.List;

public class MonthlyTransactions {
    private String month;
    private List<Purchase> purchases;
    private int totalMonthlyRewardsPoints;

    public MonthlyTransactions(String month, int totalRewardsPoints, List<Purchase> purchases) {
        this.month = month;
        this.purchases = purchases;
        this.totalMonthlyRewardsPoints = totalRewardsPoints;
    }

    public String getMonth() {
        return month;
    }

    public List<Purchase> getTransactions() {
        return purchases;
    }

    public int getTotalMonthlyRewardsPoints() {
        return totalMonthlyRewardsPoints;
    }

}
