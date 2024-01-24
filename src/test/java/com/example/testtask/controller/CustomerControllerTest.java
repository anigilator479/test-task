package com.example.testtask.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.testtask.dto.CustomerRequestDto;
import com.example.testtask.dto.CustomerResponseDto;
import com.example.testtask.dto.CustomerUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:database/delete-all-customers.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public class CustomerControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @SneakyThrows
    @Test
    @DisplayName("Add new customer with valid fields")
    void addCustomer_ValidValues_Success() {
        CustomerRequestDto customerRequestDto = new CustomerRequestDto(
                "Aboba",
                "cool-boy@gmail.com",
                "+3806943532"
        );

        String jsonRequest = objectMapper.writeValueAsString(customerRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/api/customers")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CustomerResponseDto expected = new CustomerResponseDto(
                1L,
                "Aboba",
                "cool-boy@gmail.com",
                "+3806943532"
        );

        CustomerResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CustomerResponseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.id());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @SneakyThrows
    @Test
    @DisplayName("Add new customer with invalid email")
    void addCustomer_InvalidEmail_Failure() {
        CustomerRequestDto customerRequestDto = new CustomerRequestDto(
                "Aboba",
                "coolgmail.com",
                "+3806943532"
        );

        String jsonRequest = objectMapper.writeValueAsString(customerRequestDto);

        mockMvc.perform(
                        post("/api/customers")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    @DisplayName("Add new customer with invalid phone")
    void addCustomer_InvalidPhone_Failure() {
        CustomerRequestDto customerRequestDto = new CustomerRequestDto(
                "Aboba",
                "cool@gmail.com",
                "6943532"
        );

        String jsonRequest = objectMapper.writeValueAsString(customerRequestDto);

        mockMvc.perform(
                        post("/api/customers")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    @DisplayName("Find all customers")
    @Sql(scripts = "classpath:database/add-three-customers.sql")
    void findAllCustomers_Valid_Success() {

        MvcResult result = mockMvc.perform(
                        get("/api/customers")
                )
                .andExpect(status().isOk())
                .andReturn();

        CustomerResponseDto expected1 = new CustomerResponseDto(
                1L,
                "Aboba1",
                "aboba1@gmail.com",
                "+4342253534"
        );
        CustomerResponseDto expected2 = new CustomerResponseDto(
                2L,
                "Aboba2",
                "aboba2@gmail.com",
                "+4442253534"
        );

        CustomerResponseDto expected3 = new CustomerResponseDto(
                3L,
                "Aboba3",
                "aboba3@gmail.com",
                "+4352253534"
        );

        List<CustomerResponseDto> expected = List.of(expected1, expected2, expected3);

        List<CustomerResponseDto> actual = objectMapper.readerForListOf(CustomerResponseDto.class)
                .readValue(result.getResponse().getContentAsString());
        System.out.println(expected);
        System.out.println(actual);
        assertNotNull(actual);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                    expected.get(i), actual.get(i), "id"));
        }
    }

    @SneakyThrows
    @Test
    @DisplayName("Find customer by id")
    @Sql(scripts = "classpath:database/add-customer.sql")
    void getCustomerById_ValidId_Success() {
        Long id = 1000L;

        MvcResult result = mockMvc.perform(
                        get(String.format("/api/customers/%d", id))
                )
                .andExpect(status().isOk())
                .andReturn();

        CustomerResponseDto expected = new CustomerResponseDto(
                1000L,
                "Aboba",
                "aboba@gmail.com",
                "+66666666"
        );

        CustomerResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CustomerResponseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.id());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @SneakyThrows
    @Test
    @DisplayName("Find customer by invalid id")
    void getCustomerById_InvalidId_Success() {
        Long id = -1L;

        mockMvc.perform(
                        get(String.format("/api/customers/%d", id))
                )
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    @DisplayName("Delete customer by id")
    @Sql(scripts = "classpath:database/add-customer.sql")
    void deleteCustomerById_ValidId_Success() {
        Long id = 1000L;

        mockMvc.perform(
                        delete(String.format("/api/customers/%d", id))
                )
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    @DisplayName("Update customer by id")
    @Sql(scripts = "classpath:database/add-customer.sql")
    void updateCustomer_ValidIdAndRequestDto_Success() {
        Long id = 1000L;

        CustomerUpdateRequestDto customerRequestDto = new CustomerUpdateRequestDto(
                id,
                "Aboba666",
                "+380666666"
        );

        String jsonRequest = objectMapper.writeValueAsString(customerRequestDto);

        MvcResult result = mockMvc.perform(
                        put(String.format("/api/customers/%d", id))
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CustomerResponseDto expected = new CustomerResponseDto(
                1000L,
                "Aboba666",
                "aboba@gmail.com",
                "+380666666"
        );

        CustomerResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CustomerResponseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.id());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }
}
