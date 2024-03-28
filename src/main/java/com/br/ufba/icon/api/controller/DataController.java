package com.br.ufba.icon.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Controle de dados")
@RequestMapping(value = "/api/v2/icontag")
public class DataController {

    @GetMapping(value = "/get_all_iconicos")
    public ResponseEntity<String> getAllIconicos() {
        return ResponseEntity.ok("Fetching members");
    }

}
