package com.example.testtask.dto;

public record CustomerResponseDto(
        Long id,
        String fullName,
        String email,
        String phone
) {
}
