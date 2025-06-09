package com.charter.rewards.charterrewards.dto;

import jakarta.validation.constraints.NotBlank;

public class RetailCustomer {

    private Long customerId;
    @NotBlank(message = "First name cannot be null")
    private String firstName;
    @NotBlank(message = "Last name cannot be null")
    private String lastName;
    private String email;

    public RetailCustomer() {
    }

    public RetailCustomer(Long customerId, String firstName, String lastName, String email) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
