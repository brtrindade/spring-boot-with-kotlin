package br.com.brunotrindade.springbootwithkotlin.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("RESTful API with Kotlin and Spring Boot")
                    .version("v1")
                    .description("API for spring boot studies")
                    .termsOfService("https://brunotrindade.dev")
                    .license(
                        License().name("Apache 2.0")
                            .url("https://brunotrindade.dev")
                    )
            )
    }
}