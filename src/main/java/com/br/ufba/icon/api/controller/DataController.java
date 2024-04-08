package com.br.ufba.icon.api.controller;

import com.br.ufba.icon.api.controller.dto.*;
import com.br.ufba.icon.api.domain.IconicoEntity;
import com.br.ufba.icon.api.service.IconicoService;
import com.br.ufba.icon.api.service.PointService;
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
    private final PointService pointService;

    public DataController(IconicoService iconicoService, PointService pointService) {
        this.iconicoService = iconicoService;
        this.pointService = pointService;
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

    @Operation(summary = "Encontra usuário via ID")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = IconicoEntity.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @GetMapping(value = "/get_iconico")
    public ResponseEntity<Optional<IconicoEntity>> getUser(@Valid @RequestBody int userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(iconicoService.getUserById((long) userId));
    }

    @Operation(summary = "Adiciona ponto ao usuário via UID")
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = AddPointResponse.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @PostMapping(value = "/add_point")
    public ResponseEntity<AddPointResponse> addPoint(@Valid @RequestBody AddPointRequest request) {
        AddPointResponse response = pointService.addPoint(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Recalcula horas do usuário via ID")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RecalculateHoursResponse.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @PostMapping(value = "/recalculate_hours")
    public ResponseEntity<RecalculateHoursResponse> recalculateHours(@Valid @RequestBody RecalculateHoursRequest request) {
        RecalculateHoursResponse response = iconicoService.recalculateHours(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


}
