package com.abdelrahman.ticketing.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "X-User-Id";

    @Bean
    public OpenAPI ticketingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ticketing API")
                        .description("""
                                RESTful ticketing system. \
                                Authentication is simulated via an `X-User-Id` header carrying a valid user ID; \
                                the role is looked up from the database and enforces permissions.
                                """)
                        .version("1.0.0")
                        .license(new License().name("View on GitHub")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("The ID of the authenticated user (e.g. 1 = ADMIN).")));
    }
}
