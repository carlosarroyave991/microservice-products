package com.arka.microservice.productos.domain.ports.out;

import com.arka.microservice.productos.domain.models.ProductCategoryModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductCategoryPersistencePort {
    /*Flux<ProductCategoryModel> findByCategoryId(Long id);*/
    Flux<ProductCategoryModel> findByProductId(Long id);
    Mono<ProductCategoryModel> save(ProductCategoryModel model);
}
