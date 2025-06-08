package ru.practicum.error;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private String name;
    private String url;
    private String reason;
    private Integer status;
    private String message;
    private LocalDateTime timestamp;

    public ApiError(String name, String url, Integer status, String reason, Throwable ex) {
        this.name = name;
        this.url = url;
        this.reason = reason;
        this.status = status;
        this.message = ex.getMessage();
        this.timestamp = LocalDateTime.now();
    }
}
