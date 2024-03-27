package com.br.ufba.icon.api.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Username cannot be blank")
        String username,

        @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
        @NotBlank(message = "Password cannot be blank")
        String password
) {
}
