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

    // swagger 관련 된 것을 security 제외 안 했을 때, 그룹들에 접근 가능한지 테스트용 -> 로그인 하면 접근 가능함을 확인
    @Bean
    public GroupedOpenApi viewApi() {
        return GroupedOpenApi.builder()
                .group("VIEW")
                .pathsToMatch("/**")
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
