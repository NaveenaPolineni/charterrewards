package com.charter.rewards.charterrewards.controller;

import org.springframework.web.bind.annotation.RestController;

import com.charter.rewards.charterrewards.dto.RetailCustomer;
import com.charter.rewards.charterrewards.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/api/v1/customers/{customerId}")
    public ResponseEntity<RetailCustomer> getCustomerById(@PathVariable Long customerId) {
        RetailCustomer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/api/v1/customers")
    public ResponseEntity<RetailCustomer> createCustomer(@Valid @RequestBody RetailCustomer customerRequest) {
        RetailCustomer customer = customerService.createCustomer(customerRequest);

        URI customerResourceLocation = URI.create(String.format("/customers/%s", customer.getCustomerId()));
        return ResponseEntity.created(customerResourceLocation).body(customer);
    }

    @DeleteMapping("/api/v1/customers/{customerId}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable Long customerId) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.ok("Customer with ID: " + customerId + " deleted successfully");
    }
}
