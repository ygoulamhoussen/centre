package com.formation.centre.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Centre API")
                        .version("1.0")
                        .description("API documentation for Centre application")
                        .contact(new Contact().name("Your Name").email("your.email@example.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}