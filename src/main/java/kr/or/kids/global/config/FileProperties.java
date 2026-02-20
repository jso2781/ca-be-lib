package kr.or.kids.global.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 파일 Configuration Properties Bean
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    private String storePath;
    private String url;

}
