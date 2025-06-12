package com.charter.rewards.charterrewards.controller;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.charter.rewards.charterrewards.dto.CustomerRewards;
import com.charter.rewards.charterrewards.dto.MonthlyTransactions;
import com.charter.rewards.charterrewards.exception.CustomerNotFoundException;
import com.charter.rewards.charterrewards.exception.InvalidArgumentsException;
import com.charter.rewards.charterrewards.service.RewardsService;

@WebMvcTest(RewardsController.class)
public class RewardsControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private RewardsService rewardsService;

        @Test
        @DisplayName("Returns rewards for a customer without dates")
        void testGetRewardsForCustomerWithoutDates() throws Exception {
                MonthlyTransactions mockTransaction = Mockito.mock(MonthlyTransactions.class);
                CustomerRewards mockRewards = new CustomerRewards(1L, "Naveena", "Polineni", 152,
                                List.of(mockTransaction));
                Mockito.when(rewardsService.getRewardsForCustomer(eq(1L), any(), any()))
                                .thenReturn(mockRewards);

                mockMvc.perform(get("/api/v1/customers/1/rewards"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.customerId").value(1))
                                .andExpect(jsonPath("$.firstName").value("Naveena"))
                                .andExpect(jsonPath("$.lastName").value("Polineni"))
                                .andExpect(jsonPath("$.totalRewardsPoints").value(152))
                                .andExpect(jsonPath("$.monthlyTransactions").isArray());
        }

        @Test
        @DisplayName("Returns rewards for a customer within dates range")
        void testGetRewardsForCustomerWithDates() throws Exception {
                MonthlyTransactions mockTransaction = Mockito.mock(MonthlyTransactions.class);
                CustomerRewards mockRewards = new CustomerRewards(1L, "Naveena", "Polineni", 152,
                                List.of(mockTransaction));

                Mockito.when(rewardsService.getRewardsForCustomer(eq(2L), eq(LocalDate.of(2025, 2, 1)),
                                eq(LocalDate.of(2025, 5, 31))))
                                .thenReturn(mockRewards);

                mockMvc.perform(get("/api/v1/customers/2/rewards")
                                .param("startDate", "2025-02-01")
                                .param("endDate", "2025-05-31"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("Returns 404 when customer does not exist")
        void testGetRewardsForCustomerNotFound() throws Exception {
                Mockito.when(rewardsService.getRewardsForCustomer(eq(3L), any(), any()))
                                .thenThrow(new CustomerNotFoundException(
                                                "Customer does not exist for the provided ID: 3"));

                mockMvc.perform(get("/api/v1/customers/3/rewards"))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("Customer does not exist for the provided ID: 3"));
        }

        @Test
        @DisplayName("Returns 400 when startDate is invalid")
        void testGetRewardsForCustomerInvalidStartDate() throws Exception {
                mockMvc.perform(get("/api/v1/customers/3/rewards")
                                .param("startDate", "invalid-date")
                                .param("endDate", "2025-05-31"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Returns 400 when endDate is invalid")
        void testGetRewardsForCustomerInvalidEndDate() throws Exception {
                mockMvc.perform(get("/api/v1/customers/3/rewards")
                                .param("startDate", "2025-02-01")
                                .param("endDate", "invalid-date"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Returns 400 when startDate is in the future")
        void testGetRewardsForCustomerFutureStartDate() throws Exception {
                LocalDate futureDate = LocalDate.now().plusDays(1);
                Mockito.when(rewardsService.getRewardsForCustomer(eq(3L), any(), any()))
                                .thenThrow(new InvalidArgumentsException(
                                                "Future dates are not allowed. startDate provided should be in the past or today."));
                mockMvc.perform(get("/api/v1/customers/3/rewards")
                                .param("startDate", futureDate.toString()))
                                .andExpect(status().isBadRequest())
                                .andExpect(content()
                                                .string("Future dates are not allowed. startDate provided should be in the past or today."));
        }

        @Test
        @DisplayName("Returns 400 when endDate is in the future")
        void testGetRewardsForCustomerFutureEndDate() throws Exception {
                LocalDate futureDate = LocalDate.now().plusDays(1);
                Mockito.when(rewardsService.getRewardsForCustomer(eq(3L), any(), any()))
                                .thenThrow(new InvalidArgumentsException(
                                                "Future dates are not allowed. endDate provided should be in the past or today."));
                mockMvc.perform(get("/api/v1/customers/3/rewards")
                                .param("endDate", futureDate.toString()))
                                .andExpect(status().isBadRequest())
                                .andExpect(content()
                                                .string("Future dates are not allowed. endDate provided should be in the past or today."));
        }

        @Test
        @DisplayName("Returns 400 when dates are invalid")
        void testGetRewardsForCustomerInvalidDate() throws Exception {
                mockMvc.perform(get("/api/v1/customers/3/rewards")
                                .param("startDate", "invalid-date"))
                                .andExpect(status().isBadRequest());
        }
}
