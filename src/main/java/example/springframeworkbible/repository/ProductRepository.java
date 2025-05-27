package example.springframeworkbible.repository;

import example.springframeworkbible.domain.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive repository for Product entities.
 * This interface provides methods for CRUD operations and custom queries.
 * Spring Data R2DBC will automatically implement this interface.
 */
@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    
    /**
     * Find a product by its SKU.
     *
     * @param sku The SKU to search for
     * @return A Mono containing the product if found
     */
    Mono<Product> findBySku(String sku);
    
    /**
     * Find products whose name contains the given text (case insensitive).
     *
     * @param name The name fragment to search for
     * @return A Flux of products matching the criteria
     */
    @Query("SELECT * FROM products WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Flux<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find products with a price less than or equal to the given value.
     *
     * @param maxPrice The maximum price
     * @return A Flux of products matching the criteria
     */
    Flux<Product> findByPriceLessThanEqual(Double maxPrice);
}
