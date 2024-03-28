package com.br.ufba.icon.api.controller;

import com.br.ufba.icon.api.controller.dto.AddIconicoRequest;
import com.br.ufba.icon.api.controller.dto.TimeAddRequest;
import com.br.ufba.icon.api.service.IconicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Controle de dados")
@RequestMapping(value = "/api/v2/icontag")
public class DataController {

    private final IconicoService iconicoService;

    public DataController(IconicoService iconicoService) {
        this.iconicoService = iconicoService;
    }

    @Operation(summary = "Adiciona horas ao usuário")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @PostMapping(value = "/add_iconico")
    public ResponseEntity<Void> addIconico(@Valid @RequestBody AddIconicoRequest request) {
        iconicoService.addIconico(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Adiciona horas ao usuário")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @PostMapping(value = "/add")
    public ResponseEntity<String> add6Hours(@Valid @RequestBody TimeAddRequest request) {
        iconicoService.addHoursToId((long) request.userId(), request.time());
        return ResponseEntity.ok("ok");
    }
}
