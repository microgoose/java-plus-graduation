package ru.practicum.events.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.config.DateConfig;

import java.time.LocalDateTime;
import java.util.Objects;

public class TimePresentOrFutureValidator implements ConstraintValidator<TimePresentOrFuture, String> {

    @Override
    public boolean isValid(String eventDate, ConstraintValidatorContext context) {
        if (Objects.isNull(eventDate)) {
            return true;
        }

        LocalDateTime dateTime = LocalDateTime.parse(eventDate, DateConfig.FORMATTER);
        return !dateTime.isBefore(LocalDateTime.now());
    }
}


