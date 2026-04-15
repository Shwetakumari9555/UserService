package com.UserService.dtos;

import com.UserService.entities.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterUserRequest(

        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
        String phone,

        String location,
        
        Gender gender
) {}