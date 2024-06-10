package com.br.ufba.icon.api.config;

import com.br.ufba.icon.api.interceptors.SystemStatusInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final SystemStatusInterceptor systemStatusInterceptor;

    public WebMvcConfig(SystemStatusInterceptor systemStatusInterceptor) {
        this.systemStatusInterceptor = systemStatusInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(systemStatusInterceptor);
    }
}
