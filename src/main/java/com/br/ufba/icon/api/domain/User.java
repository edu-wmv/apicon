package com.br.ufba.icon.api.domain;

public record User(
        String name,
        String email,
        String password
) {
}
