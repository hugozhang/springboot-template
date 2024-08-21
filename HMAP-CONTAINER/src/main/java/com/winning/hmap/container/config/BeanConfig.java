package com.winning.hmap.container.config;

import com.winning.hmap.container.spring.AuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Value("${me.about.widget.spring.match-url:/hmap}")
    private String matchUrl;

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> registerAuthFilter() {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(authFilter());
        registration.addUrlPatterns(matchUrl + "/*");
        registration.setName("authFilter");
        registration.setOrder(1);
        return registration;
    }

}
