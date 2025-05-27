package example.springframeworkbible.service;

import example.springframeworkbible.dto.CategoryDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for category operations.
 * This defines the business operations that can be performed on categories.
 */
public interface CategoryService {
    
    /**
     * Find all categories.
     *
     * @return A Flux of all categories
     */
    Flux<CategoryDto> findAll();
    
    /**
     * Find a category by its ID.
     *
     * @param id The ID of the category to find
     * @return A Mono containing the category if found
     */
    Mono<CategoryDto> findById(Long id);
    
    /**
     * Find a category by its name.
     *
     * @param name The name of the category to find
     * @return A Mono containing the category if found
     */
    Mono<CategoryDto> findByName(String name);
    
    /**
     * Search for categories by name (partial match, case insensitive).
     *
     * @param name The name fragment to search for
     * @return A Flux of matching categories
     */
    Flux<CategoryDto> searchByName(String name);
    
    /**
     * Create a new category.
     *
     * @param categoryDto The category data to create
     * @return A Mono containing the created category
     */
    Mono<CategoryDto> create(CategoryDto categoryDto);
    
    /**
     * Update an existing category.
     *
     * @param id The ID of the category to update
     * @param categoryDto The new category data
     * @return A Mono containing the updated category
     */
    Mono<CategoryDto> update(Long id, CategoryDto categoryDto);
    
    /**
     * Delete a category by its ID.
     *
     * @param id The ID of the category to delete
     * @return A Mono completing when the deletion is done
     */
    Mono<Void> delete(Long id);
    
    /**
     * Find all categories for a product.
     *
     * @param productId The ID of the product
     * @return A Flux of categories associated with the product
     */
    Flux<CategoryDto> findByProductId(Long productId);
} 