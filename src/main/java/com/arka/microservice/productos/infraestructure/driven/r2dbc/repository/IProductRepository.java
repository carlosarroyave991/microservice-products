package com.arka.microservice.productos.infraestructure.driven.r2dbc.repository;

import com.arka.microservice.productos.infraestructure.driven.r2dbc.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Se ocupa de las interacciones con la base de datos u otra fuente de datos,
 * generalmente utilizando entidades JPA u otro mapeo relacionado con la base de datos.
 * Puede no necesitar todos los métodos definidos en el puerto.
 */
public interface IProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
    Flux<ProductEntity> findAllByIdIn(List<Long> ids);
}
