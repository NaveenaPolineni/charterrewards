package com.charter.rewards.charterrewards.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.charter.rewards.charterrewards.dto.CustomerRewards;
import com.charter.rewards.charterrewards.service.RewardsService;

@RestController
public class RewardsController {

    private static final Logger log = LoggerFactory.getLogger(RewardsController.class);

    private final RewardsService rewardsService;

    @Autowired
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    /**
     * Retrieves rewards for a customer based on their ID and optional date range.
     *
     * @param customerId the ID of the customer
     * @param startDate  the start date of the date range (optional)
     * @param endDate    the end date of the date range (optional)
     * @return ResponseEntity containing CustomerRewards details
     */
    @GetMapping("/api/v1/customers/{customerId}/rewards")
    public ResponseEntity<CustomerRewards> getRewardsForCustomer(
            @PathVariable Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        CustomerRewards customerRewards = rewardsService.getRewardsForCustomer(customerId, startDate, endDate);

        return ResponseEntity.ok(customerRewards);
    }
}
