package kr.or.kids.global.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "surem")
public class SuremMessageProperties {

    /**
     * Surem API Base URL
     * 예) https://api.surem.com
     */
    private String baseUrl;

    /**
     * 발급받은 User Code
     */
    private String userCode;

    /**
     * 발급받은 Secret Key
     */
    private String secretKey;
}
