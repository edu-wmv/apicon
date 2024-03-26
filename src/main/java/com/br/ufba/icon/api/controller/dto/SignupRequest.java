package com.br.ufba.icon.api.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @NotBlank(message = "Name cannot be blank")
    String name,

    @Email(message = "Invalid format")
    @NotBlank(message = "Email cannot be blank")
    String email,

    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    @NotBlank(message = "Password cannot be blank")
    String password
) {
}
