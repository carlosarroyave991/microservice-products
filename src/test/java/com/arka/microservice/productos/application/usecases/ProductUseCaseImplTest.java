package com.arka.microservice.productos.application.usecases;

import com.arka.microservice.productos.domain.exception.DuplicateResourceException;
import com.arka.microservice.productos.domain.exception.ValidationException;
import com.arka.microservice.productos.domain.models.CategoryModel;
import com.arka.microservice.productos.domain.models.ProductCategoryModel;
import com.arka.microservice.productos.domain.models.ProductListModel;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseImplTest {

    @Mock
    private ProductPersistencePort productPersistencePort;
    
    @Mock
    private ProductCategoryPersistencePort productCategoryPersistencePort;
    
    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private ProductUseCaseImpl productUseCase;

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
    void findByProductId_ShouldReturnProductListModel_WhenProductExists() {
        when(productPersistencePort.findById(1L)).thenReturn(Mono.just(productModel));
        when(productCategoryPersistencePort.findByProductId(1L)).thenReturn(Flux.just(productCategoryModel));
        when(categoryPersistencePort.findAllById(Arrays.asList(1L))).thenReturn(Flux.just(categoryModel));

        StepVerifier.create(productUseCase.findByProductId(1L))
                .expectNextMatches(result -> 
                    result.getId().equals(1L) && 
                    result.getName().equals("Test Product") &&
                    result.getList().size() == 1
                )
                .verifyComplete();
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts_WhenProductsExist() {
        when(productPersistencePort.getAll()).thenReturn(Flux.just(productModel));

        StepVerifier.create(productUseCase.getAllProducts())
                .expectNext(productModel)
                .verifyComplete();
    }

    @Test
    void getAllProducts_ShouldThrowException_WhenNoProductsExist() {
        when(productPersistencePort.getAll()).thenReturn(Flux.empty());

        StepVerifier.create(productUseCase.getAllProducts())
                .expectError(DuplicateResourceException.class)
                .verify();
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct_WhenValidInput() {
        when(productPersistencePort.save(any(ProductModel.class))).thenReturn(Mono.just(productModel));

        StepVerifier.create(productUseCase.createProduct(productModel))
                .expectNext(productModel)
                .verifyComplete();
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct_WhenProductExists() {
        ProductModel updatedProduct = ProductModel.builder()
                .id(1L)
                .name("Updated Product")
                .brand("Updated Brand")
                .price(BigDecimal.valueOf(150.0))
                .stock(15)
                .build();

        when(productPersistencePort.findById(1L)).thenReturn(Mono.just(productModel));
        when(productPersistencePort.update(any(ProductModel.class))).thenReturn(Mono.just(updatedProduct));

        StepVerifier.create(productUseCase.updateProduct(updatedProduct, 1L))
                .expectNext(updatedProduct)
                .verifyComplete();
    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductNotExists() {
        when(productPersistencePort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.updateProduct(productModel, 1L))
                .expectError(DuplicateResourceException.class)
                .verify();
    }

    @Test
    void deleteProduct_ShouldComplete_WhenProductExists() {
        when(productPersistencePort.findById(1L)).thenReturn(Mono.just(productModel));
        when(productPersistencePort.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.deleteProduct(1L))
                .verifyComplete();
    }

    @Test
    void deleteProduct_ShouldThrowException_WhenProductNotExists() {
        when(productPersistencePort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.deleteProduct(1L))
                .expectError(DuplicateResourceException.class)
                .verify();
    }

    @Test
    void updateStock_ShouldComplete_WhenValidQuantity() {
        when(productPersistencePort.findById(1L)).thenReturn(Mono.just(productModel));
        when(productPersistencePort.update(any(ProductModel.class))).thenReturn(Mono.just(productModel));

        StepVerifier.create(productUseCase.updateStock(1L, 5))
                .verifyComplete();
    }

    @Test
    void updateStock_ShouldThrowException_WhenNegativeStock() {
        when(productPersistencePort.findById(1L)).thenReturn(Mono.just(productModel));

        StepVerifier.create(productUseCase.updateStock(1L, -15))
                .expectError(ValidationException.class)
                .verify();
    }

    @Test
    void getAllByIds_ShouldReturnProducts_WhenValidIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(productPersistencePort.findByIds(ids)).thenReturn(Flux.just(productModel));

        StepVerifier.create(productUseCase.getAllByIds(ids))
                .expectNext(productModel)
                .verifyComplete();
    }

    @Test
    void getAllByIds_ShouldThrowException_WhenEmptyIds() {
        StepVerifier.create(productUseCase.getAllByIds(Arrays.asList()))
                .expectError(ValidationException.class)
                .verify();
    }
}