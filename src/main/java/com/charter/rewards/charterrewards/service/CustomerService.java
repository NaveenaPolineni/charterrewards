package com.charter.rewards.charterrewards.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.charter.rewards.charterrewards.dto.RetailCustomer;
import com.charter.rewards.charterrewards.exception.CustomerNotFoundException;
import com.charter.rewards.charterrewards.model.Customer;
import com.charter.rewards.charterrewards.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public RetailCustomer getCustomerById(Long id) {
        customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        return new RetailCustomer(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail());
    }

    public RetailCustomer createCustomer(RetailCustomer customerRequest) {
        Customer customer = new Customer(customerRequest.getFirstName(),
                customerRequest.getLastName(),
                customerRequest.getEmail());
        Customer createdCustomer = customerRepository.save(customer);
        return new RetailCustomer(createdCustomer.getId(), customerRequest.getFirstName(),
                customerRequest.getLastName(), customerRequest.getEmail());

    }

    public void deleteCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        customerRepository.delete(customer);
    }

}
