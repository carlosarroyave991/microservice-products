package com.arka.microservice.productos.domain.ports.out;

import com.arka.microservice.productos.domain.models.CategoryModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Se encarga de definir las dependencias externas que el nucleo necesita.
 */
public interface CategoryPersistencePort {
    Mono<CategoryModel> findById(Long id);
    Flux<CategoryModel> findAllById(Iterable<Long> ids);
    Mono<CategoryModel> update(CategoryModel model);
    Mono<CategoryModel> save(CategoryModel model);
    Flux<CategoryModel> findAll();
    Mono<Void> deleteById(Long id);
}
