package com.arka.microservice.productos.application.usecases;

import com.arka.microservice.productos.domain.exception.DuplicateResourceException;
import com.arka.microservice.productos.domain.models.CategoryModel;
import com.arka.microservice.productos.domain.models.ProductCategoryModel;
import com.arka.microservice.productos.domain.models.ProductModel;
import com.arka.microservice.productos.domain.ports.out.CategoryPersistencePort;
import com.arka.microservice.productos.domain.ports.out.ProductCategoryPersistencePort;
import com.arka.microservice.productos.domain.ports.out.ProductPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCategoryUseCaseImplTest {

    @Mock
    private ProductCategoryPersistencePort productCategoryPersistencePort;
    
    @Mock
    private ProductPersistencePort productPersistencePort;
    
    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private ProductCategoryUseCaseImpl productCategoryUseCase;

    private ProductModel productModel;
    private CategoryModel categoryModel;
    private ProductCategoryModel productCategoryModel;

    @BeforeEach
    void setUp() {
        productModel = ProductModel.builder()
                .id(1L)
                .name("Test Product")
                .brand("Test Brand")
                .price(BigDecimal.valueOf(100.0))
                .stock(10)
                .build();

        categoryModel = CategoryModel.builder()
                .id(1L)
                .name("Test Category")
                .description("Test Description")
                .image("test.jpg")
                .createdDate(LocalDate.now())
                .build();

        productCategoryModel = ProductCategoryModel.builder()
                .productId(1L)
                .categoryId(1L)
                .build();
    }

    @Test
    void createProductCategory_ShouldReturnCreatedRelation_WhenProductAndCategoryExist() {
        when(productPersistencePort.findById(1L)).thenReturn(Mono.just(productModel));
        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.just(categoryModel));
        when(productCategoryPersistencePort.save(any(ProductCategoryModel.class)))
                .thenReturn(Mono.just(productCategoryModel));

        StepVerifier.create(productCategoryUseCase.createProductCategory(productCategoryModel))
                .expectNextMatches(result -> 
                    result.getProductId().equals(1L) && 
                    result.getCategoryId().equals(1L)
                )
                .verifyComplete();
    }

    @Test
    void createProductCategory_ShouldThrowException_WhenProductNotExists() {
        when(productPersistencePort.findById(1L)).thenReturn(Mono.empty());
        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.just(categoryModel));

        StepVerifier.create(productCategoryUseCase.createProductCategory(productCategoryModel))
                .expectError(DuplicateResourceException.class)
                .verify();
    }

    @Test
    void createProductCategory_ShouldThrowException_WhenCategoryNotExists() {
        when(productPersistencePort.findById(1L)).thenReturn(Mono.just(productModel));
        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productCategoryUseCase.createProductCategory(productCategoryModel))
                .expectError(DuplicateResourceException.class)
                .verify();
    }

    @Test
    void createProductCategory_ShouldThrowException_WhenBothNotExist() {
        when(productPersistencePort.findById(1L)).thenReturn(Mono.empty());
        when(categoryPersistencePort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productCategoryUseCase.createProductCategory(productCategoryModel))
                .expectError(DuplicateResourceException.class)
                .verify();
    }
}