package example.springframeworkbible.exception;

import example.springframeworkbible.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * Global exception handler for consistent error responses across the application.
 * This class catches exceptions and transforms them into standardized API responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException.
     *
     * @param ex The exception that was thrown
     * @param exchange The current server exchange
     * @return A standardized error response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleResourceNotFoundException(
            ResourceNotFoundException ex, ServerWebExchange exchange) {
        
        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.NOT_FOUND, 
                ex.getMessage());
        response.setPath(exchange.getRequest().getPath().value());
        
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response));
    }

    /**
     * Handles validation errors from @Valid annotations.
     *
     * @param ex The exception that was thrown
     * @param exchange The current server exchange
     * @return A standardized error response with validation errors
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleValidationException(
            WebExchangeBindException ex, ServerWebExchange exchange) {
        
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.BAD_REQUEST, 
                "Validation error: " + errorMessage);
        response.setPath(exchange.getRequest().getPath().value());
        
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response));
    }
    
    /**
     * Handles constraint violation exceptions.
     *
     * @param ex The exception that was thrown
     * @param exchange The current server exchange
     * @return A standardized error response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleConstraintViolationException(
            ConstraintViolationException ex, ServerWebExchange exchange) {
        
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        
        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.BAD_REQUEST, 
                "Validation error: " + errorMessage);
        response.setPath(exchange.getRequest().getPath().value());
        
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response));
    }

    /**
     * Catch-all handler for any unhandled exceptions.
     *
     * @param ex The exception that was thrown
     * @param exchange The current server exchange
     * @return A standardized error response
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleGlobalException(
            Exception ex, ServerWebExchange exchange) {
        
        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred: " + ex.getMessage());
        response.setPath(exchange.getRequest().getPath().value());
        
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response));
    }
} 