package com.example.project_lofi;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures allowed access points of the backend app
 * 
 * @author Gwanjin
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String CLIENT_APP_ADDRESS_1 = "http://localhost:4200";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(CLIENT_APP_ADDRESS_1).allowedMethods("GET", "POST","PUT", "DELETE");
    }
}
