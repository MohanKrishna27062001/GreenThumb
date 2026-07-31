package dev.mohan.greenthumb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank String name,
        @NotBlank @Email String email,             // must look like an email
        @NotBlank @Size(min = 8) String password   // at least 8 chars
) {}