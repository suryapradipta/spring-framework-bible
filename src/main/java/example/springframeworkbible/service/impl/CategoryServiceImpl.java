package example.springframeworkbible.service.impl;

import example.springframeworkbible.domain.Category;
import example.springframeworkbible.dto.CategoryDto;
import example.springframeworkbible.exception.ResourceNotFoundException;
import example.springframeworkbible.mapper.CategoryMapper;
import example.springframeworkbible.repository.CategoryRepository;
import example.springframeworkbible.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the CategoryService interface.
 * This class provides the business logic for category operations.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final DatabaseClient databaseClient;
    
    @Override
    public Flux<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .map(categoryMapper::toDto);
    }
    
    @Override
    public Mono<CategoryDto> findById(Long id) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.create("Category", "id", id)))
                .map(categoryMapper::toDto);
    }
    
    @Override
    public Mono<CategoryDto> findByName(String name) {
        return categoryRepository.findByName(name)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.create("Category", "name", name)))
                .map(categoryMapper::toDto);
    }
    
    @Override
    public Flux<CategoryDto> searchByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name)
                .map(categoryMapper::toDto);
    }
    
    @Override
    @Transactional
    public Mono<CategoryDto> create(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryRepository.save(category)
                .map(categoryMapper::toDto);
    }
    
    @Override
    @Transactional
    public Mono<CategoryDto> update(Long id, CategoryDto categoryDto) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.create("Category", "id", id)))
                .map(existingCategory -> categoryMapper.updateEntityFromDto(existingCategory, categoryDto))
                .flatMap(categoryRepository::save)
                .map(categoryMapper::toDto);
    }
    
    @Override
    @Transactional
    public Mono<Void> delete(Long id) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.create("Category", "id", id)))
                .flatMap(category -> 
                    // Delete all product-category relationships
                    databaseClient.sql("DELETE FROM product_categories WHERE category_id = :categoryId")
                            .bind("categoryId", id)
                            .fetch()
                            .rowsUpdated()
                            .then(categoryRepository.delete(category))
                );
    }
    
    @Override
    public Flux<CategoryDto> findByProductId(Long productId) {
        return categoryRepository.findCategoriesByProductId(productId)
                .map(categoryMapper::toDto);
    }
} 