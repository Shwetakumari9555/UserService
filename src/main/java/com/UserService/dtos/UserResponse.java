package com.UserService.dtos;

import java.util.UUID;

import com.UserService.entities.Gender;
import com.UserService.entities.Provider;
import com.UserService.entities.Role;
import lombok.Builder;

@Builder
public record UserResponse(

        UUID userId,
        String name,
        String email,
        String profilePic,
        String phone,
        String location,
        boolean enabled,
        Gender gender,
        Provider provider,
        Role role
) {}