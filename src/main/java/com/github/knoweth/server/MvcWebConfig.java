package com.github.knoweth.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcWebConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Register resource handler for JS
        registry.addResourceHandler("/").addResourceLocations("/index.html");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "file:./target/generated/")
                .setCacheControl(CacheControl.noCache()); // TODO enable caching
    }
}
