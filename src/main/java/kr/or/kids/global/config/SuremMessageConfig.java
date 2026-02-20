package kr.or.kids.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class SuremMessageConfig {

    private final SuremMessageProperties properties;

    @Bean
    public WebClient suremWebClient() {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("usercode", properties.getUserCode())
                .defaultHeader("secretkey", properties.getSecretKey())
                .build();
    }
}
