package ru.practicum.events.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimePresentOrFutureValidator.class)
public @interface TimePresentOrFuture {
    String message() default "Дата должна быть в настоящем или будущем времени";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

