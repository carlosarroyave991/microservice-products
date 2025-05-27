package com.arka.microservice.productos.domain.ports.in;

import com.arka.microservice.productos.domain.models.CategoryModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Se definen las operaciones REST que pueden utilizar para interactuar
 * con el nucreo del sistema.
 */
public interface ICategoryPortUseCase {
    Mono<CategoryModel> updateCategory(CategoryModel model, Long id);
    Flux<CategoryModel> getAllCategories();
    Mono<CategoryModel> createCategory(CategoryModel model);
    Mono<Void> deleteCategory(Long id);
}
