package ru.practicum.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
@Getter
public class DateConfig {

    @Value("${spring.jackson.date-format}")
    private String dateFormat;

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern(dateFormat);
    }
}
