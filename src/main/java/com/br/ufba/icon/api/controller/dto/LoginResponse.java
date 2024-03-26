package com.br.ufba.icon.api.controller.dto;

public record LoginResponse(
        String email,
        String token
) {
}
