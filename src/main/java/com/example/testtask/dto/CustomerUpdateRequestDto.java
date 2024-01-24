package com.example.testtask.dto;

import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record CustomerUpdateRequestDto(
        Long id,
        @Length(min = 2, max = 50)
        String fullName,
        @Pattern(regexp = "^\\+\\d+$", message = "Invalid phone number")
        @Length(min = 6, max = 14)
        String phone
) {

}
