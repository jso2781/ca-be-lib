package kr.or.kids.global.exception;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    private Logger logger = LoggerFactory.getLogger(CustomErrorAttributes.class);

    private final MessageSource messageSource;

    public CustomErrorAttributes(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> base = super.getErrorAttributes(webRequest, options);

        int status = (int) base.getOrDefault("status", 500);
        String path = (String) base.getOrDefault("path", "");
        String traceId = UUID.randomUUID().toString();

        var locale = LocaleContextHolder.getLocale();
        String msgKey = statusMessageKey(status);
        String userMessage = messageSource.getMessage(
            msgKey,
            null,
            messageSource.getMessage("api.error.default", null, locale),
            locale
        );

        logger.info("CustomErrorAttributes getErrorAttributes base=",base);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", status);
        body.put("error", base.getOrDefault("error", ""));
        body.put("message", userMessage);
        body.put("path", path);
        body.put("traceId", traceId);

        // 운영에서는 내부 상세 노출 금지 권장
        body.put("detail", base.get("message"));
        body.put("exception", base.get("exception"));
        body.put("stackTrace", base.get("trace"));
        return body;
    }

    private String statusMessageKey(int status) {
        switch (status) {
            case 400: return "api.error.400";
            case 403: return "api.error.403";
            case 500: return "api.error.500";
            default:  return "api.error.default";
        }
    }
}
