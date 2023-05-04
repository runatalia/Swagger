package com.example.userservice.config.swagger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;


@AllArgsConstructor
@Getter
public class SwaggerProperties {
    @Value("${swagger.title}")
    private final String projectTitle;
    @Value("${swagger.description}")
    private final String projectDescription;
    @Value("${swagger.version}")
    private final String projectVersion;
}