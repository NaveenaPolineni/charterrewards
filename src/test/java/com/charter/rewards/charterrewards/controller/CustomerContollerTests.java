package com.charter.rewards.charterrewards.controller;

import com.charter.rewards.charterrewards.dto.RetailCustomer;
import com.charter.rewards.charterrewards.exception.CustomerNotFoundException;
import com.charter.rewards.charterrewards.service.CustomerService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerContollerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    @DisplayName("Returns customer details when customer exists")
    void testGetCustomerById() throws Exception {
        RetailCustomer customer = new RetailCustomer();
        customer.setCustomerId(1L);
        customer.setFirstName("Naveena");
        customer.setLastName("Polineni");

        Mockito.when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.firstName").value("Naveena"))
                .andExpect(jsonPath("$.lastName").value("Polineni"));
    }

    @Test
    @DisplayName("Returns 404 when customer does not exist")
    void testGetCustomerById_NotFound() throws Exception {
        Mockito.when(customerService.getCustomerById(99L))
                .thenThrow(new CustomerNotFoundException("Customer not found with ID: 99"));

        mockMvc.perform(get("/api/v1/customers/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found with ID: 99"));
    }

    @Test
    @DisplayName("Will return 400 when request body is invalid")
    void testCreateCustomer_BadRequest() throws Exception {
        String invalidJson = """
                {
                    "firstName": "",
                    "lastName": ""
                }
                """;

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Will return 201 when customer is created successfully")
    void testCreateCustomer() throws Exception {
        RetailCustomer customer = new RetailCustomer();
        customer.setFirstName("Naveena");
        customer.setLastName("Polineni");

        RetailCustomer response = new RetailCustomer();
        response.setCustomerId(2L);
        response.setFirstName("Naveena");
        response.setLastName("Polineni");
        response.setEmail("naveenam2203@gmail.com");

        Mockito.when(customerService.createCustomer(any(RetailCustomer.class))).thenReturn(response);

        String json = """
                {
                    "firstName": "Naveena",
                    "lastName": "Polineni",
                    "email": "naveenam2203@gmail.com"
                }
                                """;

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/customers/2"))
                .andExpect(jsonPath("$.customerId").value(2L))
                .andExpect(jsonPath("$.firstName").value("Naveena"))
                .andExpect(jsonPath("$.lastName").value("Polineni"))
                .andExpect(jsonPath("$.email").value("naveenam2203@gmail.com"));

    }

    @Test
    void testDeleteCustomerById() throws Exception {
        Mockito.doNothing().when(customerService).deleteCustomerById(3L);

        mockMvc.perform(delete("/api/v1/customers/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer with ID: 3 deleted successfully"));
    }

}