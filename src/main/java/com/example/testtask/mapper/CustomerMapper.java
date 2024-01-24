package com.example.testtask.mapper;

import com.example.testtask.dto.CustomerRequestDto;
import com.example.testtask.dto.CustomerResponseDto;
import com.example.testtask.dto.CustomerUpdateRequestDto;
import com.example.testtask.model.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl")
public interface CustomerMapper {
    CustomerResponseDto toResponse(Customer customer);

    Customer toModel(CustomerRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomer(CustomerUpdateRequestDto requestDto, @MappingTarget Customer customer);
}
