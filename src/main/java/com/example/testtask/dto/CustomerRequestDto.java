package com.example.testtask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record CustomerRequestDto(
        @Length(min = 2, max = 50)
        String fullName,
        @Email
        @Length(min = 2, max = 100)
        String email,
        @Pattern(regexp = "^\\+\\d+$", message = "Invalid phone number")
        @Length(min = 6, max = 14)
        String phone
) {
}
