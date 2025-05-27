package com.arka.microservice.productos.application.usecases;

import com.arka.microservice.productos.domain.exception.DuplicateResourceException;
import com.arka.microservice.productos.domain.models.CategoryModel;
import com.arka.microservice.productos.domain.ports.in.ICategoryPortUseCase;
import com.arka.microservice.productos.domain.ports.out.CategoryPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Date;

import static com.arka.microservice.productos.domain.exception.error.CommonErrorCode.*;

/**
 * Clase usada para implementar la logica de negocio sobre cada funcion
 */
@Service
@RequiredArgsConstructor
public class CategoryUseCaseImpl implements ICategoryPortUseCase {
    private final CategoryPersistencePort service;

    /**
     * Servicio que permite actualizar una categoria especifica de manera reactiva
     * @param model objeto con la data a actualizar
     * @param id identificador del objeto
     * @return retorna un Mono con la categoria actualizada o error si no existe
     */
    @Override
    public Mono<CategoryModel> updateCategory(CategoryModel model, Long id) {
        return service.findById(id)
                .switchIfEmpty(Mono.error(new DuplicateResourceException(ID_NOT_FOUND)))
                .flatMap(existing -> {
                    existing.setId(id);
                    if (model.getName() != null)existing.setName(model.getName());
                    if (model.getDescription() != null)existing.setDescription(model.getDescription());
                    if (model.getImage() != null)existing.setImage(model.getImage());
                    if (model.getCreatedDate() != null)existing.setCreatedDate(model.getCreatedDate());
                    existing.setUpdatedDate(LocalDate.now());
                    return service.save(existing);
                });
    }

    /**
     * Servicio que obtiene todas las categorias existentes de forma reactiva
     * @return returna un Flux que emite cada category o error si la lista esta vacia
     */
    @Override
    public Flux<CategoryModel> getAllCategories() {
        return service.findAll()
                .switchIfEmpty(Flux.error(new DuplicateResourceException(DB_EMPTY)));
    }

    /**
     * Servicio usado para crear una categoria de forma reactiva.
     * @param model objeto category con los parámetros necesarios para la creación.
     * @return retorna un Mono con la categoria creada o un error.
     */
    @Override
    public Mono<CategoryModel> createCategory(CategoryModel model) {
        model.setCreatedDate(LocalDate.now());
        return service.save(model);
    }

    /**
     * Servicio usado para eliminar un objeto de forma reactiva
     * @param id identificador del objeto
     * @return retorna un Mono<Void> que completa o emite un error si el objeto no existe.
     */
    @Override
    public Mono<Void> deleteCategory(Long id) {
        return service.findById(id)
                .switchIfEmpty(Mono.error(new DuplicateResourceException(DB_EMPTY)))
                .flatMap(existing -> service.deleteById(id));
    }
}
