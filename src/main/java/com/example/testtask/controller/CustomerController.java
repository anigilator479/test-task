package com.example.testtask.controller;

import com.example.testtask.dto.CustomerRequestDto;
import com.example.testtask.dto.CustomerResponseDto;
import com.example.testtask.dto.CustomerUpdateRequestDto;
import com.example.testtask.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer management", description = "Endpoints for managing customers")
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @Operation(summary = "Get all customers",
            description = "Returns a list of all customers")
    @GetMapping
    public List<CustomerResponseDto> getAll(
            @PageableDefault(sort = "fullName") Pageable pageable) {
        return customerService.findAll(pageable);
    }

    @Operation(summary = "Get customer by id",
            description = "Returns a customer by his id")
    @GetMapping("/{id}")
    public CustomerResponseDto getById(@PathVariable @Positive Long id) {
        return customerService.getById(id);
    }

    @Operation(summary = "Delete customer by id",
            description = "Deletes customer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive Long id) {
        customerService.deleteById(id);
    }

    @Operation(summary = "Create new customer",
            description = "Creates a new customer in db")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CustomerResponseDto create(@RequestBody @Valid CustomerRequestDto requestDto) {
        return customerService.save(requestDto);
    }

    @Operation(summary = "Update customer info by id",
            description = "Updates data about customer in the db by id")
    @PutMapping("/{id}")
    public CustomerResponseDto update(@RequestBody @Valid CustomerUpdateRequestDto requestDto,
                                          @PathVariable @Positive Long id) {
        return customerService.updateById(requestDto, id);
    }
}
