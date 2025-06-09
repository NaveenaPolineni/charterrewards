package com.charter.rewards.charterrewards.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.charter.rewards.charterrewards.dto.RetailCustomer;
import com.charter.rewards.charterrewards.exception.CustomerNotFoundException;
import com.charter.rewards.charterrewards.model.Customer;
import com.charter.rewards.charterrewards.repository.CustomerRepository;
import com.charter.rewards.charterrewards.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({ MockitoExtension.class })
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private RetailCustomer retailCustomer;

    @BeforeEach
    void setUp() {
        customer = new Customer("Naveena", "Polineni", "naveenam2203@gmail.com");
        customer.setId(1L);
        retailCustomer = new RetailCustomer(1L, "Naveena", "Polineni", "naveenam2203@gmail.com");
    }

    @Test
    @DisplayName("Get Customer By Id returns Customer Successfully")
    void testGetCustomerByIdReturnsCustomerSuccessfully() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        RetailCustomer result = customerService.getCustomerById(1L);
        assertNotNull(result);
        assertEquals(retailCustomer.getCustomerId(), result.getCustomerId());
        assertEquals(retailCustomer.getFirstName(), result.getFirstName());
        assertEquals(retailCustomer.getLastName(), result.getLastName());
        assertEquals(retailCustomer.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("Get Customer By Id throws CustomerNotFoundException when customer does not exist")
    void testGetCustomerByIdThrowsNotFoundException() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(2L));
    }

    @Test
    @DisplayName("Create Customer returns Customer Successfully")
    void testCreateCustomerCreatesCustomerSuccessfully() {
        RetailCustomer request = new RetailCustomer(null, "Charter", "Communications",
                "charter.communications@retailer.com");
        Customer savedCustomer = new Customer("Charter", "Communications", "charter.communications@retailer.com");
        savedCustomer.setId(2L);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        RetailCustomer result = customerService.createCustomer(request);

        assertNotNull(result);
        assertEquals(savedCustomer.getId(), result.getCustomerId());
        assertEquals(savedCustomer.getFirstName(), result.getFirstName());
        assertEquals(savedCustomer.getLastName(), result.getLastName());
        assertEquals(savedCustomer.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("Delete Customer By Id throws CustomerNotFoundException when customer does not exist")
    void testDeleteCustomerByIdThrowsNotFoundException() {
        when(customerRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomerById(3L));
    }

    @Test
    @DisplayName("Delete Customer By Id deletes customer successfully")
    void testDeleteCustomerByIdDeletesSuccessfully() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        assertDoesNotThrow(() -> customerService.deleteCustomerById(1L));
        verify(customerRepository, times(1)).delete(customer);
    }
}
