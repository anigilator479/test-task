package com.example.testtask.service;

import com.example.testtask.dto.CustomerRequestDto;
import com.example.testtask.dto.CustomerResponseDto;
import com.example.testtask.dto.CustomerUpdateRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerResponseDto save(CustomerRequestDto customerRequestDto);

    List<CustomerResponseDto> findAll(Pageable pageable);

    CustomerResponseDto getById(Long id);

    CustomerResponseDto updateById(CustomerUpdateRequestDto customerRequestDto, Long id);

    void deleteById(Long id);
}
