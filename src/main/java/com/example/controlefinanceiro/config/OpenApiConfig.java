package com.example.controlefinanceiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
    
    @Bean
    /**
     * Configuração personalizada para o OpenAPI.
     * @return Instância do OpenAPI configurada.
     */
    OpenAPI customOpenAPI() {
        final String apiTitle = "RESTful API with Java";
        final String apiVersion = "v1";
        final String apiDescription = "Description of API";
        final String termsOfServiceUrl = "http://springcourse.com.br/swagger";
        final String licenseName = "Apache 2.0";
        final String licenseUrl = "http://springcourse.com.br/swagger"; 

        return new OpenAPI()
            .info(new Info()
                .title(apiTitle)
                .version(apiVersion)
                .description(apiDescription)
                .termsOfService(termsOfServiceUrl)
                .license(new License().name(licenseName).url(licenseUrl)));
    }
}

