package example.springframeworkbible.mapper;

import example.springframeworkbible.domain.Product;
import example.springframeworkbible.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Mapper for converting between Product entities and DTOs.
 * This class separates the domain model from the API model.
 */
@Component
public class ProductMapper {
    
    /**
     * Converts a Product entity to a ProductDto.
     *
     * @param product The Product entity to convert
     * @return The corresponding ProductDto
     */
    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .sku(product.getSku())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .categories(Collections.emptyList()) // Categories are fetched separately
                .build();
    }
    
    /**
     * Converts a ProductDto to a Product entity.
     *
     * @param productDto The ProductDto to convert
     * @return The corresponding Product entity
     */
    public Product toEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        
        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .sku(productDto.getSku())
                .build();
    }
    
    /**
     * Updates a Product entity with values from a ProductDto.
     *
     * @param product The Product entity to update
     * @param productDto The ProductDto containing new values
     * @return The updated Product entity
     */
    public Product updateEntityFromDto(Product product, ProductDto productDto) {
        if (product == null || productDto == null) {
            return product;
        }
        
        if (productDto.getName() != null) {
            product.setName(productDto.getName());
        }
        
        if (productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());
        }
        
        if (productDto.getPrice() != null) {
            product.setPrice(productDto.getPrice());
        }
        
        if (productDto.getSku() != null) {
            product.setSku(productDto.getSku());
        }
        
        return product;
    }
} 