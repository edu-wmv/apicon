package com.br.ufba.icon.api.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

//    Server localServer = new Server()
//            .url("localhost:6000")
//            .description("API Docs");

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createApiKeyScheme()))
//                .addServersItem(localServer)
                .info(apiInfo());
    }

    private SecurityScheme createApiKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    private Info apiInfo() {
        return new Info()
                .title("Lab ICON API Doc")
                .version("2.0.0")
                .description("API para requisições HTTP ao banco de dados do Lab ICON")
                .contact(new Contact()
                        .name("Lab ICON")
                        .email("icon@ufba.br")
                        .url("https://icon.ufba.br/")
                );
    }
}
