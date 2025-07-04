package com.arka.microservice.productos.application.usecases;

import com.arka.microservice.productos.domain.exception.DuplicateResourceException;
import com.arka.microservice.productos.domain.exception.NotFoundException;
import com.arka.microservice.productos.domain.models.CategoryModel;
import com.arka.microservice.productos.domain.ports.out.CategoryPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryUseCaseImplTest {

    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private CategoryUseCaseImpl categoryUseCase;

    private CategoryModel categoryModel;

    @BeforeEach
    void setUp() {
        categoryModel = CategoryModel.builder()
                .id(1L)
                .name("Test Category")
                .description("Test Description")
                .image("test.jpg")
                .createdDate(LocalDate.now())
                .updatedDate(LocalDate.now())
                .build();
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories_WhenCategoriesExist() {
        when(categoryPersistencePort.findAll()).thenReturn(Flux.just(categoryModel));

        StepVerifier.create(categoryUseCase.getAllCategories())
                .expectNext(categoryModel)
                .verifyComplete();
    }

    @Test
    void getAllCategories_ShouldThrowException_WhenNoCategoriesExist() {
        when(categoryPersistencePort.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(categoryUseCase.getAllCategories())
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void createCategory_ShouldReturnCreatedCategory_WhenValidInput() {
        CategoryModel newCategory = CategoryModel.builder()
                .name("New Category")
                .description("New Description")
                .image("new.jpg")
                .build();

        CategoryModel savedCategory = CategoryModel.builder()
                .id(1L)
                .name("New Category")
                .description("New Description")
                .image("new.jpg")
                .createdDate(LocalDate.now())
                .build();

        when(categoryPersistencePort.save(any(CategoryModel.class))).thenReturn(Mono.just(savedCategory));

        StepVerifier.create(categoryUseCase.createCategory(newCategory))
                .expectNextMatches(result -> 
                    result.getName().equals("New Category") &&
                    result.getCreatedDate() != null
                )
                .verifyComplete();
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategory_WhenCategoryExists() {
        CategoryModel updatedCategory = CategoryModel.builder()
                .id(1L)
                .name("Updated Category")
                .description("Updated Description")
                .image("updated.jpg")
                .createdDate(LocalDate.now())
                .updatedDate(LocalDate.now())
                .build();

        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.just(categoryModel));
        when(categoryPersistencePort.save(any(CategoryModel.class))).thenReturn(Mono.just(updatedCategory));

        StepVerifier.create(categoryUseCase.updateCategory(updatedCategory, 1L))
                .expectNextMatches(result -> 
                    result.getName().equals("Updated Category") &&
                    result.getUpdatedDate() != null
                )
                .verifyComplete();
    }

    @Test
    void updateCategory_ShouldThrowException_WhenCategoryNotExists() {
        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(categoryUseCase.updateCategory(categoryModel, 1L))
                .expectError(DuplicateResourceException.class)
                .verify();
    }

    @Test
    void deleteCategory_ShouldComplete_WhenCategoryExists() {
        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.just(categoryModel));
        when(categoryPersistencePort.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(categoryUseCase.deleteCategory(1L))
                .verifyComplete();
    }

    @Test
    void deleteCategory_ShouldThrowException_WhenCategoryNotExists() {
        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(categoryUseCase.deleteCategory(1L))
                .expectError(DuplicateResourceException.class)
                .verify();
    }
}