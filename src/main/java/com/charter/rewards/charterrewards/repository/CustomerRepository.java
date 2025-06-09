package com.charter.rewards.charterrewards.repository;

import com.charter.rewards.charterrewards.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
}
