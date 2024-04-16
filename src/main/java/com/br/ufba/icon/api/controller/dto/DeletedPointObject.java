package com.br.ufba.icon.api.controller.dto;

import java.sql.Timestamp;

public record DeletedPointObject(
        String id,
        Timestamp date,
        Boolean status,
        String username,
        String uid,
        Long user_id,
        String reason
) {
}
