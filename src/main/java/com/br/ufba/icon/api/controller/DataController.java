package com.br.ufba.icon.api.controller;

import com.br.ufba.icon.api.controller.dto.AddIconicoRequest;
import com.br.ufba.icon.api.controller.dto.TimeAddRequest;
import com.br.ufba.icon.api.controller.dto.TimeAddResponse;
import com.br.ufba.icon.api.domain.IconicoEntity;
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

import java.util.Optional;

@RestController
@Tag(name = "Controle de dados")
@RequestMapping(value = "/api/v2/icontag")
public class DataController {

    private final IconicoService iconicoService;

    public DataController(IconicoService iconicoService) {
        this.iconicoService = iconicoService;
    }

    @Operation(summary = "Adiciona usuário")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @PostMapping(value = "/add_iconico")
    public ResponseEntity<IconicoEntity> addIconico(@Valid @RequestBody AddIconicoRequest request) {
        IconicoEntity addedIconico = iconicoService.addIconico(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(addedIconico);
    }

    @Operation(summary = "Adiciona horas ao usuário pelo ID")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @PostMapping(value = "/add_hours")
    public ResponseEntity<TimeAddResponse> addHours(@Valid @RequestBody TimeAddRequest request) {
        TimeAddResponse addHours = iconicoService.addHoursToId((long) request.userId(), request.time());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addHours);
    }

    @Operation(summary = "Encontra usuário via ID")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @GetMapping(value = "/get_iconico")
    public ResponseEntity<Optional<IconicoEntity>> getUser(@Valid @RequestBody int userId) {
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(iconicoService.getUserById((long) userId));
    }



}
