package com.br.ufba.icon.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.br.ufba.icon.api.controller.dto.ApiErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AuthControllerIntegrationTest {

    static final String SIGNUP_URL = "/api/v2/auth/signup";
    static final String LOGIN_URL = "/api/v2/auth/login";
    private static final int VALIDATION_ERROR_CODE = 400;

    private final WebTestClient webTestClient;

    public AuthControllerIntegrationTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @BeforeEach
    public void cleanUp() {

    }

    @Test
    public void shouldSignupUser() {
        String request = """
                {
                    "name": "test",
                    "email": "test@test.br"
                    "password": "test"
                }
                """;

        webTestClient
                .post().uri(SIGNUP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void shouldReturnDuplicate_onExistingEmail() {
        String request = """
                {
                    "name": "joao",
                    "email": "joao@test.br"
                    "password": "123"
                }
                """;

        webTestClient
                .post().uri(SIGNUP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isCreated();

        String req2 = """
                {
                    "name": "maria",
                    "email": "joao@test.br"
                    "password": "456"
                }
                """;

        ApiErrorResponse errorResponse = webTestClient
                .post().uri(SIGNUP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req2)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(ApiErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.errorCode()).isEqualTo(409);
        assertThat(errorResponse.description()).isEqualTo("User with the email address 'joao@test.br' already exists.");
    }
}
