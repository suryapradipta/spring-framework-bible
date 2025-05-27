package example.springframeworkbible.controller;

import example.springframeworkbible.dto.ApiResponse;
import example.springframeworkbible.dto.CategoryDto;
import example.springframeworkbible.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * REST Controller for Category resources.
 * This class defines the API endpoints for category operations.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    /**
     * Get all categories.
     *
     * @return A response containing all categories
     */
    @GetMapping
    public Mono<ApiResponse<List<CategoryDto>>> getAllCategories() {
        return categoryService.findAll()
                .collectList()
                .map(categories -> ApiResponse.success(categories, "Categories retrieved successfully"));
    }
    
    /**
     * Get a category by ID.
     *
     * @param id The ID of the category to retrieve
     * @return A response containing the category
     */
    @GetMapping("/{id}")
    public Mono<ApiResponse<CategoryDto>> getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(category -> ApiResponse.success(category, "Category retrieved successfully"));
    }
    
    /**
     * Get a category by name.
     *
     * @param name The name of the category to retrieve
     * @return A response containing the category
     */
    @GetMapping("/name/{name}")
    public Mono<ApiResponse<CategoryDto>> getCategoryByName(@PathVariable String name) {
        return categoryService.findByName(name)
                .map(category -> ApiResponse.success(category, "Category retrieved successfully"));
    }
    
    /**
     * Search categories by name.
     *
     * @param name The name fragment to search for
     * @return A response containing matching categories
     */
    @GetMapping("/search")
    public Mono<ApiResponse<List<CategoryDto>>> searchCategories(@RequestParam String name) {
        return categoryService.searchByName(name)
                .collectList()
                .map(categories -> ApiResponse.success(categories, "Categories retrieved successfully"));
    }
    
    /**
     * Create a new category.
     *
     * @param categoryDto The category data to create
     * @return A response containing the created category
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponse<CategoryDto>> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.create(categoryDto)
                .map(category -> ApiResponse.success(category, "Category created successfully"));
    }
    
    /**
     * Update an existing category.
     *
     * @param id The ID of the category to update
     * @param categoryDto The new category data
     * @return A response containing the updated category
     */
    @PutMapping("/{id}")
    public Mono<ApiResponse<CategoryDto>> updateCategory(
            @PathVariable Long id, 
            @Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto)
                .map(category -> ApiResponse.success(category, "Category updated successfully"));
    }
    
    /**
     * Delete a category.
     *
     * @param id The ID of the category to delete
     * @return A response indicating success
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCategory(@PathVariable Long id) {
        return categoryService.delete(id);
    }
    
    /**
     * Get all categories for a product.
     *
     * @param productId The ID of the product
     * @return A response containing categories for the product
     */
    @GetMapping("/product/{productId}")
    public Mono<ApiResponse<List<CategoryDto>>> getCategoriesByProductId(@PathVariable Long productId) {
        return categoryService.findByProductId(productId)
                .collectList()
                .map(categories -> ApiResponse.success(categories, "Categories retrieved successfully"));
    }
} 