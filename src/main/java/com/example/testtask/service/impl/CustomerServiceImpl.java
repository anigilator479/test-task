package com.example.testtask.service.impl;

import com.example.testtask.dto.CustomerRequestDto;
import com.example.testtask.dto.CustomerResponseDto;
import com.example.testtask.dto.CustomerUpdateRequestDto;
import com.example.testtask.exception.CustomerNotFoundException;
import com.example.testtask.exception.DifferentIdValuesException;
import com.example.testtask.mapper.CustomerMapper;
import com.example.testtask.model.Customer;
import com.example.testtask.repository.CustomerRepository;
import com.example.testtask.service.CustomerService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponseDto save(CustomerRequestDto customerRequestDto) {
        Customer customer = customerMapper.toModel(customerRequestDto);
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    public List<CustomerResponseDto> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable).stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerResponseDto getById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Can't find customer with this id: %d", id)));
    }

    @Override
    public CustomerResponseDto updateById(CustomerUpdateRequestDto customerRequestDto, Long id) {
        validateIdFields(customerRequestDto.id(), id);

        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new CustomerNotFoundException(
                        String.format("Can't update customer with this id: %d", id)));

        customerMapper.updateCustomer(customerRequestDto, customer);
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    private static void validateIdFields(Long requestDtoId, Long id) {
        if (!Objects.equals(requestDtoId, id)) {
            throw new DifferentIdValuesException(String.format(
                    "Id values can't be different: %d and %d", requestDtoId, id));
        }
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }
}
