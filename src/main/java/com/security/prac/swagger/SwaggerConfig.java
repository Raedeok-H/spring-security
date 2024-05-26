package com.security.prac.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// swagger-ui.html 경로로 접근 가능
@Configuration
public class SwaggerConfig {

    // TODO: 로그인과 게시판 기능이 나오면 하위 엔드포인트로 분리할 것
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("API")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI getOpenApi() {
        return new OpenAPI().components(new Components())
                .info(getInfo());
    }

    private Info getInfo() {
        return new Info()
                .version("1.0.0")
                .description("BE-API-DOC")
                .title("BE-API");
    }
}
