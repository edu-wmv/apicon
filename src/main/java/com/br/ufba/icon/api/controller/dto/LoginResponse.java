package com.br.ufba.icon.api.controller.dto;

public record LoginResponse(
        String username,
        String token,
        String expiresAt
) {
}
