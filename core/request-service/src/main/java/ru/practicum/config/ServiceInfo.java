package ru.practicum.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceInfo {

    @Value("${spring.application.name}")
    private String serviceName;

}
