package com.springriders.perfume.config;

import com.springriders.perfume.interceptor.AdminInterceptor;
import com.springriders.perfume.interceptor.JoinInterceptor;
import com.springriders.perfume.interceptor.LoginInterceptor;
import com.springriders.perfume.interceptor.MyPageInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/user/admin/*");
        registry.addInterceptor(new MyPageInterceptor())
                .addPathPatterns("/user/myPage/*");
        registry.addInterceptor(new JoinInterceptor())
                .addPathPatterns("/user/join/*");
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/user/login/*");
    }
}
