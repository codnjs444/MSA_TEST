package com.example.userService.global.message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.rakugakibox.util.YamlResourceBundle;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
public class MessageConfig implements WebMvcConfigurer {

    private final Environment env;

    public MessageConfig(Environment env) {
        this.env = env;
    }

    /**
     * 세션에 지역(locale) 설정. 기본값은 한국어(ko)
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.KOREAN);
        return slr;
    }

    /**
     * 요청 헤더(X-LANG)에 lang 정보가 있으면 locale을 변경해 주는 인터셉터
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        return new LocaleChangeInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Object handler) {
                String newLocale = request.getHeader("X-LANG");
                if (newLocale != null) {
                    LocaleResolver resolver = RequestContextUtils.getLocaleResolver(request);
                    if (resolver != null) {
                        resolver.setLocale(request, response, StringUtils.parseLocaleString(newLocale));
                    }
                }
                return true;
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * YAML 기반 메시지 번들(MessageSource) 빈 등록
     * Environment를 통해 spring.messages.basename, spring.messages.encoding 값을 조회한다.
     */
    @Bean
    public MessageSource messageSource() {
        // application-dev.yml 등에 설정된 spring.messages.basename 값을 꺼내오고,
        // 만약 없으면 기본값을 "message/exception"으로 사용한다.
        String basename = env.getProperty("spring.messages.basename", "message/exception");
        String encoding = env.getProperty("spring.messages.encoding", "UTF-8");

        YamlMessageSource ms = new YamlMessageSource();
        ms.setBasename(basename);
        ms.setDefaultEncoding(encoding);
        ms.setAlwaysUseMessageFormat(true);
        ms.setUseCodeAsDefaultMessage(true);
        ms.setFallbackToSystemLocale(true);
        return ms;
    }

    /**
     * ResourceBundleMessageSource를 상속하여 YAML 리소스를 읽어들이도록 커스터마이징
     */
    private static class YamlMessageSource extends ResourceBundleMessageSource {
        @Override
        protected ResourceBundle doGetBundle(String baseName, Locale locale) {
            return ResourceBundle.getBundle(baseName, locale, YamlResourceBundle.Control.INSTANCE);
        }
    }
}
