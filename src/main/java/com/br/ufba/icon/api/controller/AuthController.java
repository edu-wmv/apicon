package com.br.ufba.icon.api.controller;

import com.br.ufba.icon.api.controller.dto.LoginRequest;
import com.br.ufba.icon.api.controller.dto.LoginResponse;
import com.br.ufba.icon.api.controller.dto.SignupRequest;
import com.br.ufba.icon.api.helper.JwtHelper;
import com.br.ufba.icon.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Controle de autenticação")
@RequestMapping(path = "/api/v2/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Cadastra novo usuário no banco de dados")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @PostMapping(value = "/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest requestDto) {
        userService.signup(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User created!");
    }

    @Operation(summary = "Autentica usuário e retorna chave JWT")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    @PostMapping(value = "/login")
    // error trigger due '@RequestBody' (check later) [????]
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody @NotNull LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        String token = JwtHelper.generateToken(request.username());
        String expiration = JwtHelper.getTokenExpiration(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new LoginResponse(request.username(), token, expiration));
    }
}
