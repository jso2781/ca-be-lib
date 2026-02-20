package kr.or.kids.global.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import io.swagger.v3.oas.annotations.Hidden;

@Controller
@Hidden
public class CustomErrorController implements ErrorController
{
    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    /**
     * ✅ 어떤 클라이언트(브라우저 포함)든 /error는 JSON만 반환
     */
    @RequestMapping(value = "${server.error.path}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {

        ServletWebRequest webRequest = new ServletWebRequest(request);

        // 운영 안전: 예외/스택트레이스 포함 X
//        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        ErrorAttributeOptions options = ErrorAttributeOptions.of(
                ErrorAttributeOptions.Include.MESSAGE,
                ErrorAttributeOptions.Include.EXCEPTION
                // StackTrace까지 보고 싶으면 아래도 추가
                , ErrorAttributeOptions.Include.STACK_TRACE
        );

        Map<String, Object> body = errorAttributes.getErrorAttributes(webRequest, options);

        int status = (int) body.getOrDefault("status", 500);

        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(body);
    }
}