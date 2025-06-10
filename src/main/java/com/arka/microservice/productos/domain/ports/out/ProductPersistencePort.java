package com.arka.microservice.productos.domain.ports.out;

import com.arka.microservice.productos.domain.models.ProductModel;
import com.arka.microservice.productos.domain.models.ProductStockModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Se encarga de definir las dependencias externas que el nucleo necesita.
 */
public interface ProductPersistencePort {
    Mono<ProductModel> findById(Long id);
    Mono<ProductModel> update(ProductModel model);
    Mono<ProductModel> save(ProductModel model);
    Flux<ProductModel> getAll();
    Mono<Void> deleteById(Long id);
    Mono<Void> updateStock(ProductModel model);
}
