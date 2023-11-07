package com.example.springtrash.common.config;


import com.example.springtrash.common.filter.LoginSessionFilter;
import javax.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginSessionFilter loginSessionFilter;


    @Bean
    public FilterRegistrationBean<LoginSessionFilter> loginFilter(){
        FilterRegistrationBean<LoginSessionFilter> loginFilter = new FilterRegistrationBean<>();
        loginFilter.setFilter(loginSessionFilter);
        loginFilter.addUrlPatterns("/members", "/members/my");

        return loginFilter;
    }

}
