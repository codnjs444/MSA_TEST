package com.example.userService.global.message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.rakugakibox.util.YamlResourceBundle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
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

	@Bean // 세션에 지역설정. default는 KOREAN = 'ko'
    LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.KOREAN);
        return slr;
    }

	@Bean // 지역설정을 변경하는 인터셉터. 요청시 헤더(X-LANG)에 lang 정보를 지정하면 언어가 변경됨.
    LocaleChangeInterceptor localeChangeInterceptor() {
        return new LocaleChangeInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String newLocale = request.getHeader("X-LANG");
                if (newLocale != null) {
                    LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
                    if (localeResolver != null) {
                        localeResolver.setLocale(request, response, StringUtils.parseLocaleString(newLocale));
                    }
                }
                return true;
            }
        };
    }

    @Override // 인터셉터를 시스템 레지스트리에 등록
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean // yml 파일을 참조하는 MessageSource 선언
    MessageSource messageSource(
            @Value("${spring.messages.basename}") String basename,
            @Value("${spring.messages.encoding}") String encoding
    ) {
        YamlMessageSource ms = new YamlMessageSource();
        ms.setBasename(basename);
        ms.setDefaultEncoding(encoding);
        ms.setAlwaysUseMessageFormat(true);
        ms.setUseCodeAsDefaultMessage(true);
        ms.setFallbackToSystemLocale(true);
        return ms;
    }

    // locale 정보에 따라 다른 yml 파일을 읽도록 처리
    private static class YamlMessageSource extends ResourceBundleMessageSource {
        @Override
        protected ResourceBundle doGetBundle(String basename, Locale locale) {
            return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
        }
    }
}
