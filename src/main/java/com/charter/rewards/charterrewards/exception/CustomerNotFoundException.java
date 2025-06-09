package com.charter.rewards.charterrewards.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long customerId) {
        super("Customer with ID " + customerId + " not found.");
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
    
}
