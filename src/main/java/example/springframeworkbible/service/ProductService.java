package example.springframeworkbible.service;

import example.springframeworkbible.dto.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for product operations.
 * This defines the business operations that can be performed on products.
 */
public interface ProductService {
    
    /**
     * Find all products.
     *
     * @return A Flux of all products
     */
    Flux<ProductDto> findAll();
    
    /**
     * Find a product by its ID.
     *
     * @param id The ID of the product to find
     * @return A Mono containing the product if found
     */
    Mono<ProductDto> findById(Long id);
    
    /**
     * Find a product by its SKU.
     *
     * @param sku The SKU of the product to find
     * @return A Mono containing the product if found
     */
    Mono<ProductDto> findBySku(String sku);
    
    /**
     * Search for products by name (partial match, case insensitive).
     *
     * @param name The name fragment to search for
     * @return A Flux of matching products
     */
    Flux<ProductDto> searchByName(String name);
    
    /**
     * Create a new product.
     *
     * @param productDto The product data to create
     * @return A Mono containing the created product
     */
    Mono<ProductDto> create(ProductDto productDto);
    
    /**
     * Update an existing product.
     *
     * @param id The ID of the product to update
     * @param productDto The new product data
     * @return A Mono containing the updated product
     */
    Mono<ProductDto> update(Long id, ProductDto productDto);
    
    /**
     * Delete a product by its ID.
     *
     * @param id The ID of the product to delete
     * @return A Mono completing when the deletion is done
     */
    Mono<Void> delete(Long id);
    
    /**
     * Add a category to a product.
     *
     * @param productId The product ID
     * @param categoryId The category ID
     * @return A Mono containing the updated product
     */
    Mono<ProductDto> addCategory(Long productId, Long categoryId);
    
    /**
     * Remove a category from a product.
     *
     * @param productId The product ID
     * @param categoryId The category ID
     * @return A Mono containing the updated product
     */
    Mono<ProductDto> removeCategory(Long productId, Long categoryId);
} 