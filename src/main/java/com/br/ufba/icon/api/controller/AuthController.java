package com.br.ufba.icon.api.controller;

import com.br.ufba.icon.api.controller.dto.LoginRequest;
import com.br.ufba.icon.api.controller.dto.LoginResponse;
import com.br.ufba.icon.api.controller.dto.SignupRequest;
import com.br.ufba.icon.api.helper.JwtHelper;
import com.br.ufba.icon.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v2/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return authenticationManager;
    }

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String token = JwtHelper.generateToken(request.email());
        return ResponseEntity.ok(new LoginResponse(request.email(), token));
    }
}
