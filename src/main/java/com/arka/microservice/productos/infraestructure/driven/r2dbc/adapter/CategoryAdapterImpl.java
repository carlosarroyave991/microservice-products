package com.arka.microservice.productos.infraestructure.driven.r2dbc.adapter;

import com.arka.microservice.productos.domain.models.CategoryModel;
import com.arka.microservice.productos.domain.ports.in.ICategoryPortUseCase;
import com.arka.microservice.productos.domain.ports.out.CategoryPersistencePort;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.entity.CategoryEntity;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.mapper.ICategoryEntityMapper;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * El adaptador se encargara de conectar ambas capas, pasando la informacion de la entidad
 * al modelo de dominio.
 */
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CategoryAdapterImpl implements CategoryPersistencePort {
    private final ICategoryRepository repository;
    private final ICategoryEntityMapper mapper;

    /**
     * Funcion que consulta un objeto por id y lo mapea a modelo
     * @param id identificador el objeto a buscar
     * @return retorna un objeto mapeado para dominio
     */
    @Override
    public Mono<CategoryModel> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toModel);
    }

    /**
     * Funcion que consulta las categories que pertenecen a un product
     * @param ids identificadores de las categories
     * @return retorna flux con lista de categories
     */
    @Override
    public Flux<CategoryModel> findAllById(Iterable<Long> ids) {
        return repository.findAllById(ids)
                .map(mapper::toModel);
    }

    /**
     * Funcion actualiza un objeto existente. Se busca por id
     * @param model objeto a actualizar, contiene un id en sus atributos
     * @return retorna un objeto mapeado para el dominio
     */
    @Override
    public Mono<CategoryModel> update(CategoryModel model) {
        CategoryEntity entity = mapper.toEntity(model);
        return repository.save(entity)
                .map(mapper::toModel);
    }

    /**
     * Funcion que guarda un objeto
     * @param model objeto a guardar
     * @return retorna un objeto mapeado para el dominio
     */
    @Override
    public Mono<CategoryModel> save(CategoryModel model) {
        CategoryEntity entity = mapper.toEntity(model);
        return repository.save(entity)
                .map(mapper::toModel);
    }

    /**
     * Funcion que consulta todos los objetos y los mapea a modelo.
     * @return retorna todos los objetos mapeados para el dominio
     */
    @Override
    public Flux<CategoryModel> findAll() {
        return repository.findAll()
                .map(mapper::toModel);
    }

    /**
     * Funcion que eliminar un objeto dado su id y retorna el Mono<Void> correspondiente.
     * @param id identificador del objeto a eliminar
     */
    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }
}
