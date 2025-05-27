package example.springframeworkbible.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Standard API response format for all REST endpoints.
 * This ensures consistent response structure across the application.
 * 
 * @param <T> The type of data being returned in the response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private HttpStatus status;
    
    private Integer statusCode;
    
    private String message;
    
    private T data;
    
    private String path;
    
    /**
     * Creates a successful response with data.
     * 
     * @param data The data to include in the response
     * @param message A message describing the successful operation
     * @param <T> The type of data
     * @return A fully constructed ApiResponse
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }
    
    /**
     * Creates a successful response without data.
     * 
     * @param message A message describing the successful operation
     * @param <T> The type of data (none in this case)
     * @return A fully constructed ApiResponse
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .build();
    }
    
    /**
     * Creates an error response.
     * 
     * @param status The HTTP status code for the error
     * @param message A message describing the error
     * @param <T> The type of data (none in this case)
     * @return A fully constructed ApiResponse
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .statusCode(status.value())
                .message(message)
                .build();
    }
} 