package example.springframeworkbible.mapper;

import example.springframeworkbible.domain.Category;
import example.springframeworkbible.dto.CategoryDto;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Category entities and DTOs.
 * This class separates the domain model from the API model.
 */
@Component
public class CategoryMapper {
    
    /**
     * Converts a Category entity to a CategoryDto.
     *
     * @param category The Category entity to convert
     * @return The corresponding CategoryDto
     */
    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
    
    /**
     * Converts a CategoryDto to a Category entity.
     *
     * @param categoryDto The CategoryDto to convert
     * @return The corresponding Category entity
     */
    public Category toEntity(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .build();
    }
    
    /**
     * Updates a Category entity with values from a CategoryDto.
     *
     * @param category The Category entity to update
     * @param categoryDto The CategoryDto containing new values
     * @return The updated Category entity
     */
    public Category updateEntityFromDto(Category category, CategoryDto categoryDto) {
        if (category == null || categoryDto == null) {
            return category;
        }
        
        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }
        
        if (categoryDto.getDescription() != null) {
            category.setDescription(categoryDto.getDescription());
        }
        
        return category;
    }
} 