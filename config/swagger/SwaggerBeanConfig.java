package com.example.userservice.config.swagger;

import org.springframework.context.annotation.Bean;

public class SwaggerBeanConfig {
    @Bean
    ApiInfoProvider apiInfoProvider(SwaggerProperties swaggerProperties) {
        return new ApiInfoProvider(swaggerProperties);
    }
}
