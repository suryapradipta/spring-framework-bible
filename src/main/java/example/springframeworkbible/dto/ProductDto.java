package example.springframeworkbible.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Product entities.
 * This class is used for API requests and responses, separating the API model from the domain model.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private String name;
    
    private String description;
    
    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be positive")
    private BigDecimal price;
    
    @NotBlank(message = "Product SKU is required")
    @Size(min = 3, max = 50, message = "SKU must be between 3 and 50 characters")
    private String sku;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private List<CategoryDto> categories;
} 