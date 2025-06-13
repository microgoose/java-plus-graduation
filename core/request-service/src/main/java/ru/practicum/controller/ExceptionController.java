package ru.practicum.controller;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.config.ServiceInfo;
import ru.practicum.error.ApiError;
import ru.practicum.error.ForbiddenActionException;
import ru.practicum.error.NotFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    private final ServiceInfo serviceInfo;

    @ExceptionHandler
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Bad request");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleBadRequest(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Bad request");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleBadRequest(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Bad request");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND, "Not found");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleBusinessError(ForbiddenActionException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.CONFLICT, "Business error");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleBusinessError(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.CONFLICT, "Business error");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleFeignException(FeignException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "External error");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleUnknownException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error");
    }

    private ResponseEntity<ApiError> buildErrorResponse(Exception ex, HttpServletRequest request,
                                                        HttpStatus status, String message) {

        return buildErrorResponse(ex, new ApiError(serviceInfo.getServiceName(),
                getRequestUrl(request), status.value(), message, ex));
    }

    private ResponseEntity<ApiError> buildErrorResponse(Exception ex, ApiError apiError) {
        log.error(String.format("%s on %s", apiError.getMessage(), apiError.getUrl()), ex);
        return ResponseEntity
                .status(apiError.getStatus())
                .body(apiError);
    }

    private String getRequestUrl(HttpServletRequest request) {
        return request.getMethod() + " " + request.getRequestURL().toString();
    }
}
