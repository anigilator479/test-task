package com.example.testtask.service;

import com.example.testtask.dto.CustomerRequestDto;
import com.example.testtask.dto.CustomerResponseDto;
import com.example.testtask.dto.CustomerUpdateRequestDto;
import com.example.testtask.exception.CustomerNotFoundException;
import com.example.testtask.exception.DifferentIdValuesException;
import com.example.testtask.mapper.CustomerMapper;
import com.example.testtask.mapper.impl.CustomerMapperImpl;
import com.example.testtask.model.Customer;
import com.example.testtask.repository.CustomerRepository;
import com.example.testtask.service.impl.CustomerServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Spy
    private CustomerMapper customerMapper = new CustomerMapperImpl();

    @Test
    @DisplayName("Verify the correct book returns when book exists")
    public void getCustomerById_WithValidId_Success() {
        Long customerId = 1L;

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setIsActive(true);
        customer.setPhone("+38052343");
        customer.setCreated(System.currentTimeMillis());
        customer.setUpdated(System.currentTimeMillis());
        customer.setEmail("aboba@gmail.com");
        customer.setFullName("Aboba");

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerResponseDto expected = new CustomerResponseDto(
                1L,
                "Aboba",
                "aboba@gmail.com",
                "+38052343"
        );

        CustomerResponseDto actual = customerService.getById(customerId);

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("Verify the correct exception message returns with not valid id")
    public void getCustomerById_WithNotValidId_Failure() {
        Long customerId = 1000L;

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(CustomerNotFoundException.class,
                () -> customerService.getById(customerId));

        String expected = String.format("Can't find customer with this id: %d", customerId);

        String actual = exception.getMessage();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify the correct customer returns after customer was created")
    public void createCustomer_Valid_Success() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setIsActive(true);
        customer.setPhone("+88005553535");
        customer.setCreated(System.currentTimeMillis());
        customer.setUpdated(System.currentTimeMillis());
        customer.setEmail("aboba@gmail.com");
        customer.setFullName("Aboba");

        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        CustomerResponseDto expected = new CustomerResponseDto(
                1L,
                "Aboba",
                "aboba@gmail.com",
                "+88005553535"
        );

        CustomerRequestDto customerRequestDto = new CustomerRequestDto(
                "Aboba",
                "aboba@gmail.com",
                "+88005553535"
        );

        CustomerResponseDto actual = customerService.save(customerRequestDto);

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("Verify correct delete method invocation")
    public void deleteCustomerById_ValidId_Success() {
        Long id = 1L;

        Mockito.doNothing().when(customerRepository).deleteById(id);

        customerService.deleteById(id);

        Mockito.verify(customerRepository).deleteById(id);
        Mockito.verifyNoMoreInteractions(customerRepository);
    }

    @Test
    @DisplayName("Verify correct work of updating method")
    public void updateCustomerById_ValidId_Success() {
        Long customerId = 1L;

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setIsActive(true);
        customer.setPhone("+88005553535");
        customer.setCreated(System.currentTimeMillis());
        customer.setUpdated(System.currentTimeMillis());
        customer.setEmail("aboba@gmail.com");
        customer.setFullName("Aboba");

        CustomerResponseDto expected = new CustomerResponseDto(
                1L,
                "Aboba123",
                "aboba@gmail.com",
                "+88005553"
        );

        Mockito.when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));
        Mockito.when(customerRepository.save(customer))
                .thenReturn(customer);

        CustomerUpdateRequestDto customerRequestDto = new CustomerUpdateRequestDto(
                customerId,
                "Aboba123",
                "+88005553"
        );

        CustomerResponseDto actual = customerService.updateById(customerRequestDto, customerId);

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Verify correct error throws with different id's")
    public void updateBookById_invalidId_Failure() {
        Long customerId = 10L;

        CustomerUpdateRequestDto customerRequestDto = new CustomerUpdateRequestDto(
                11L,
                "Aboba123",
                "+88005553"
        );

        Assertions.assertThrows(DifferentIdValuesException.class,
                () -> customerService.updateById(customerRequestDto, customerId));
    }
}
