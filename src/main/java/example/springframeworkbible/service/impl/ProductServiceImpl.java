package example.springframeworkbible.service.impl;

import example.springframeworkbible.domain.Product;
import example.springframeworkbible.dto.CategoryDto;
import example.springframeworkbible.dto.ProductDto;
import example.springframeworkbible.exception.ResourceNotFoundException;
import example.springframeworkbible.mapper.CategoryMapper;
import example.springframeworkbible.mapper.ProductMapper;
import example.springframeworkbible.repository.CategoryRepository;
import example.springframeworkbible.repository.ProductRepository;
import example.springframeworkbible.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the ProductService interface.
 * This class provides the business logic for product operations.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final DatabaseClient databaseClient;
    
    @Override
    public Flux<ProductDto> findAll() {
        return productRepository.findAll()
                .map(productMapper::toDto)
                .flatMap(this::enrichProductWithCategories);
    }
    
    @Override
    public Mono<ProductDto> findById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.create("Product", "id", id)))
                .map(productMapper::toDto)
                .flatMap(this::enrichProductWithCategories);
    }
    
    @Override
    public Mono<ProductDto> findBySku(String sku) {
        return productRepository.findBySku(sku)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.create("Product", "sku", sku)))
                .map(productMapper::toDto)
                .flatMap(this::enrichProductWithCategories);
    }
    
    @Override
    public Flux<ProductDto> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .map(productMapper::toDto)
                .flatMap(this::enrichProductWithCategories);
    }
    
    @Override
    @Transactional
    public Mono<ProductDto> create(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        return productRepository.save(product)
                .map(productMapper::toDto)
                .flatMap(this::enrichProductWithCategories);
    }
    
    @Override
    @Transactional
    public Mono<ProductDto> update(Long id, ProductDto productDto) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.create("Product", "id", id)))
                .map(existingProduct -> productMapper.updateEntityFromDto(existingProduct, productDto))
                .flatMap(productRepository::save)
                .map(productMapper::toDto)
                .flatMap(this::enrichProductWithCategories);
    }
    
    @Override
    @Transactional
    public Mono<Void> delete(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.create("Product", "id", id)))
                .flatMap(product -> 
                    // Delete all product-category relationships
                    databaseClient.sql("DELETE FROM product_categories WHERE product_id = :productId")
                            .bind("productId", id)
                            .fetch()
                            .rowsUpdated()
                            .then(productRepository.delete(product))
                );
    }
    
    @Override
    @Transactional
    public Mono<ProductDto> addCategory(Long productId, Long categoryId) {
        // First, ensure both product and category exist
        Mono<Product> productMono = productRepository.findById(productId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.create("Product", "id", productId)));
        
        Mono<Void> categoryExistsMono = categoryRepository.findById(categoryId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.create("Category", "id", categoryId)))
                .then();
        
        // Then, add the relationship
        return Mono.zip(productMono, categoryExistsMono)
                .flatMap(tuple -> 
                    databaseClient.sql("INSERT INTO product_categories (product_id, category_id) VALUES (:productId, :categoryId) ON CONFLICT DO NOTHING")
                            .bind("productId", productId)
                            .bind("categoryId", categoryId)
                            .fetch()
                            .rowsUpdated()
                            .thenReturn(tuple.getT1())
                )
                .map(productMapper::toDto)
                .flatMap(this::enrichProductWithCategories);
    }
    
    @Override
    @Transactional
    public Mono<ProductDto> removeCategory(Long productId, Long categoryId) {
        // First, ensure both product and category exist
        Mono<Product> productMono = productRepository.findById(productId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.create("Product", "id", productId)));
        
        Mono<Void> categoryExistsMono = categoryRepository.findById(categoryId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.create("Category", "id", categoryId)))
                .then();
        
        // Then, remove the relationship
        return Mono.zip(productMono, categoryExistsMono)
                .flatMap(tuple -> 
                    databaseClient.sql("DELETE FROM product_categories WHERE product_id = :productId AND category_id = :categoryId")
                            .bind("productId", productId)
                            .bind("categoryId", categoryId)
                            .fetch()
                            .rowsUpdated()
                            .thenReturn(tuple.getT1())
                )
                .map(productMapper::toDto)
                .flatMap(this::enrichProductWithCategories);
    }
    
    /**
     * Helper method to enrich a product with its categories.
     *
     * @param productDto The product to enrich
     * @return The enriched product
     */
    private Mono<ProductDto> enrichProductWithCategories(ProductDto productDto) {
        return categoryRepository.findCategoriesByProductId(productDto.getId())
                .map(categoryMapper::toDto)
                .collectList()
                .doOnNext(productDto::setCategories)
                .thenReturn(productDto);
    }
} 