package kr.or.kids.global.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sensemail")
public class SenseMailProperties {

    private String baseUrl;
    private String apiKey;
}