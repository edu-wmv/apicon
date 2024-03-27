package com.br.ufba.icon.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v2/icontag")
public class DataController {

    @GetMapping(value = "/data")
    public static ResponseEntity<String> data() {
        return ResponseEntity.ok("ok");
    }

}
