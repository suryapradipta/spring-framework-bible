package example.springframeworkbible.controller;

import example.springframeworkbible.dto.ApiResponse;
import example.springframeworkbible.dto.ProductDto;
import example.springframeworkbible.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * REST Controller for Product resources.
 * This class defines the API endpoints for product operations.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    /**
     * Get all products.
     *
     * @return A response containing all products
     */
    @GetMapping
    public Mono<ApiResponse<List<ProductDto>>> getAllProducts() {
        return productService.findAll()
                .collectList()
                .map(products -> ApiResponse.success(products, "Products retrieved successfully"));
    }
    
    /**
     * Get a product by ID.
     *
     * @param id The ID of the product to retrieve
     * @return A response containing the product
     */
    @GetMapping("/{id}")
    public Mono<ApiResponse<ProductDto>> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(product -> ApiResponse.success(product, "Product retrieved successfully"));
    }
    
    /**
     * Get a product by SKU.
     *
     * @param sku The SKU of the product to retrieve
     * @return A response containing the product
     */
    @GetMapping("/sku/{sku}")
    public Mono<ApiResponse<ProductDto>> getProductBySku(@PathVariable String sku) {
        return productService.findBySku(sku)
                .map(product -> ApiResponse.success(product, "Product retrieved successfully"));
    }
    
    /**
     * Search products by name.
     *
     * @param name The name fragment to search for
     * @return A response containing matching products
     */
    @GetMapping("/search")
    public Mono<ApiResponse<List<ProductDto>>> searchProducts(@RequestParam String name) {
        return productService.searchByName(name)
                .collectList()
                .map(products -> ApiResponse.success(products, "Products retrieved successfully"));
    }
    
    /**
     * Create a new product.
     *
     * @param productDto The product data to create
     * @return A response containing the created product
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.create(productDto)
                .map(product -> ApiResponse.success(product, "Product created successfully"));
    }
    
    /**
     * Update an existing product.
     *
     * @param id The ID of the product to update
     * @param productDto The new product data
     * @return A response containing the updated product
     */
    @PutMapping("/{id}")
    public Mono<ApiResponse<ProductDto>> updateProduct(
            @PathVariable Long id, 
            @Valid @RequestBody ProductDto productDto) {
        return productService.update(id, productDto)
                .map(product -> ApiResponse.success(product, "Product updated successfully"));
    }
    
    /**
     * Delete a product.
     *
     * @param id The ID of the product to delete
     * @return A response indicating success
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(@PathVariable Long id) {
        return productService.delete(id);
    }
    
    /**
     * Add a category to a product.
     *
     * @param productId The product ID
     * @param categoryId The category ID
     * @return A response containing the updated product
     */
    @PostMapping("/{productId}/categories/{categoryId}")
    public Mono<ApiResponse<ProductDto>> addCategoryToProduct(
            @PathVariable Long productId,
            @PathVariable Long categoryId) {
        return productService.addCategory(productId, categoryId)
                .map(product -> ApiResponse.success(product, "Category added to product successfully"));
    }
    
    /**
     * Remove a category from a product.
     *
     * @param productId The product ID
     * @param categoryId The category ID
     * @return A response containing the updated product
     */
    @DeleteMapping("/{productId}/categories/{categoryId}")
    public Mono<ApiResponse<ProductDto>> removeCategoryFromProduct(
            @PathVariable Long productId,
            @PathVariable Long categoryId) {
        return productService.removeCategory(productId, categoryId)
                .map(product -> ApiResponse.success(product, "Category removed from product successfully"));
    }
}
