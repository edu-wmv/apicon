package com.br.ufba.icon.api.controller.dto;

import java.sql.Timestamp;

public record TimeAddResponse(

        Long id,
        String username,
        Timestamp time
) {
}
