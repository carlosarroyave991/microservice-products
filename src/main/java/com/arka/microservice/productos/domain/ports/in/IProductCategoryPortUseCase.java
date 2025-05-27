package com.arka.microservice.productos.domain.ports.in;

import com.arka.microservice.productos.domain.models.ProductCategoryModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Se encarga de definir las dependencias externas que el nucleo necesita.
 */
public interface IProductCategoryPortUseCase {
    Mono<ProductCategoryModel> createProductCategory(ProductCategoryModel model);
}
