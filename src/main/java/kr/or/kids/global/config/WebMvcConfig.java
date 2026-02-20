package kr.or.kids.global.config;

import java.util.Locale;

import javax.servlet.MultipartConfigElement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxRequestSize;

    @Value("${spring.servlet.multipart.file-size-threshold}")
    private String fileSizeThreshold;

    public WebMvcConfig()
    {
        logger.info("WebMvcConfig 생성됨.");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                // 개발 환경을 위한 기본 허용. 운영 시에는 실제 UI 도메인으로 제한하세요.
                .allowedOrigins("http://localhost:5173", "http://localhost:3000","http://192.168.2.149:30012")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS").allowedHeaders("*")
                .allowCredentials(true);
        
        // OZ 리포트 서블릿을 위한 CORS 설정
        registry.addMapping("/oz90/**")
                .allowedOrigins("http://localhost:5173","http://localhost:3000","http://192.168.2.149:30012")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    /**
     * messages.properties / messages_ko.properties / messages_en.properties 로딩
     * - STS ResourceBundle Editor가 이 파일들을 묶어서 관리하기 좋음
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:message/messages");
        ms.setDefaultEncoding("UTF-8");
        ms.setCacheSeconds(3); // 개발 시 빠른 반영용 (운영은 -1 또는 충분히 길게)
        ms.setFallbackToSystemLocale(false);
        return ms;
    }

    /**
     * Bean Validation이 위 MessageSource를 사용하도록 연결
     * (Validation 에러 메시지도 messages*.properties에서 다국어로 처리됨)
     */
    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    /**
     * Locale 결정 방법 (쿠키 기반)
     * - 기본 로케일 ko
     */
//    @Bean
//    LocaleResolver localeResolver() {
//        CookieLocaleResolver resolver = new CookieLocaleResolver(); // new CookieLocaleResolver("APP_LOCALE");
//        resolver.setDefaultLocale(Locale.KOREAN);
//        resolver.setCookieMaxAge(60 * 60 * 24 * 30); // 30일
//        return resolver;
//    }

    /**
     * HTTP 요청의 Accept-Language 헤더에 지정된 기본 로케일을 사용하는 LocaleResolver
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.KOREAN);
        return resolver;
    }

    /**
     * lang 파라미터로 Locale 변경
     * 예: /hello?lang=en
     */
    @Bean
    LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor i = new LocaleChangeInterceptor();
        i.setParamName("lang");
        return i;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    MultipartResolver multipartResolver()
    {
        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
        return multipartResolver;
    }

    @Bean
    MultipartConfigElement multipartConfigElement()
    {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse(maxFileSize));
        factory.setMaxRequestSize(DataSize.parse(maxRequestSize));
        factory.setFileSizeThreshold(DataSize.parse(fileSizeThreshold));
//       cmr.setDefaultEncoding("UTF-8");  // important to match in your client
//        cmr.getFileUpload().setProgressListener(
//            (long pBytesRead, long pContentLength, int pItems) -> {
//              // insert progress update logic here
//            });
//
//        return cmr;
        return factory.createMultipartConfig();
    }

//    @Bean("emailTemplateEngine")
//    SpringTemplateEngine emailTemplateEngine()
//    {
//        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.addTemplateResolver(emailHtmlContentTemplateResolver());
//        return templateEngine;
//    }
//
//    private ITemplateResolver emailHtmlContentTemplateResolver() {
//        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setOrder(1);
////      templateResolver.setResolvablePatterns(Collections.singleton("text/*"));
//        templateResolver.setPrefix("/templates/email/");
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode(TemplateMode.HTML);
//        templateResolver.setCharacterEncoding("UTF-8");
//        templateResolver.setCacheable(false);
//
//        return templateResolver;
//    }
}
