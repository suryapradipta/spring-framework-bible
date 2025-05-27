package example.springframeworkbible.repository;

import example.springframeworkbible.domain.Category;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive repository for Category entities.
 * This interface provides methods for CRUD operations and custom queries.
 * Spring Data R2DBC will automatically implement this interface.
 */
@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
    
    /**
     * Find a category by its name.
     *
     * @param name The name to search for
     * @return A Mono containing the category if found
     */
    Mono<Category> findByName(String name);
    
    /**
     * Find categories whose name contains the given text (case insensitive).
     *
     * @param name The name fragment to search for
     * @return A Flux of categories matching the criteria
     */
    @Query("SELECT * FROM categories WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Flux<Category> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find categories associated with a given product.
     *
     * @param productId The ID of the product
     * @return A Flux of categories associated with the product
     */
    @Query("SELECT c.* FROM categories c " +
           "JOIN product_categories pc ON c.id = pc.category_id " +
           "WHERE pc.product_id = :productId")
    Flux<Category> findCategoriesByProductId(Long productId);
}
