package com.security.prac.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//TODO: 배포시 삭제할 것
@Controller
public class SwaggerController {
    // 개발할 동안만 사용할 테스트 API
    // 홈으로 접속하면 swagger-ui.html 로 리다이렉트
    @GetMapping(path="/")
    public String swagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
