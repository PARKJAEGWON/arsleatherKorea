package com.groo.kmw.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/")
                //캐시하는 시간 설정
                .setCachePeriod(3600)
                .resourceChain(true);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/kmw/uploads/")
                .setCachePeriod(3600)  // 이미지도 캐시 설정
                .resourceChain(true);
    }
}