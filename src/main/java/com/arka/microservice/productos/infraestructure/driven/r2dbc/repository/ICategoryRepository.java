package com.arka.microservice.productos.infraestructure.driven.r2dbc.repository;

import com.arka.microservice.productos.domain.models.CategoryModel;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.entity.CategoryEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Se ocupa de las interacciones con la base de datos u otra fuente de datos,
 * generalmente utilizando entidades JPA u otro mapeo relacionado con la base de datos.
 * Puede no necesitar todos los métodos definidos en el puerto.
 */
public interface ICategoryRepository extends ReactiveCrudRepository<CategoryEntity, Long> {
    Mono<CategoryEntity> findById(Long id);
    Mono<Void> deleteById(Long id);
}
