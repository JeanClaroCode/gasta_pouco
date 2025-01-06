package br.com.jeanclaro.gasta_pouco.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){

        return new OpenAPI()
        .info(new Info()
            .title("Gestão de Gastos")
            .description("API responsável por gestão de gastos")
            .version("1"))
        .schemaRequirement("jwt_auth", creaSecurityScheme())
        ;
    }

    private SecurityScheme creaSecurityScheme(){
        return new SecurityScheme()
        .name("jwt_auth")
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT");
    }
}
