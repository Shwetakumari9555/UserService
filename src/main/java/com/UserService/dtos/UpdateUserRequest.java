package com.UserService.dtos;

import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record UpdateUserRequest(
        UUID userId,
        String name,
        String profilePic,

        @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
        String phone,

        String location
) {}