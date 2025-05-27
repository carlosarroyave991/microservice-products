package com.arka.microservice.productos.infraestructure.driven.r2dbc.adapter;

import com.arka.microservice.productos.domain.models.ProductModel;
import com.arka.microservice.productos.domain.ports.out.ProductPersistencePort;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.entity.ProductEntity;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.mapper.IProductEntityMapper;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.repository.IProductRepository;
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
public class ProductAdapterImpl implements ProductPersistencePort {
    private final IProductRepository repository;
    private final IProductEntityMapper mapper;

    /**
     * Funcion que eliminar un objeto dado su id y retorna el Mono<Void> correspondiente.
     * @param id identificador del objeto a eliminar
     */
    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    /**
     * Funcion que consulta todos los objetos y los mapea a modelo.
     * @return retorna todos los objetos mapeados para el dominio
     */
    @Override
    public Flux<ProductModel> getAll() {
        return repository.findAll()
                .map(mapper::toModel);
    }

    /**
     * Funcion que guarda un objeto
     * @param model objeto a guardar
     * @return retorna un objeto mapeado para el dominio
     */
    @Override
    public Mono<ProductModel> save(ProductModel model) {
        log.info("Convirtiendo modelo a entidad");
        ProductEntity entity = mapper.toEntity(model);
        log.info("Entidad generada a partir del modelo: {}", entity);
        return repository.save(entity)
                .doOnNext(savedEntity -> log.info("Entidad guardada en el repositorio: {}", savedEntity))
                .map(mapper::toModel)
                .doOnNext(modelConverted -> log.info("Modelo convertido desde la entidad guardada: {}", modelConverted))
                .doOnError(error -> log.error("Error al guardar la entidad en el repositorio", error));
    }

    /**
     * Funcion actualiza un objeto existente. Se busca por id
     * @param model objeto a actualizar, contiene un id en sus atributos
     * @return retorna un objeto mapeado para el dominio
     */
    @Override
    public Mono<ProductModel> update(ProductModel model) {
        ProductEntity entity = mapper.toEntity(model);
        return repository.save(entity)
                .map(mapper::toModel);
    }

    /**
     * Funcion que consulta un objeto por id y lo mapea a modelo
     * @param id identificador el objeto a buscar
     * @return retorna un objeto mapeado para dominio
     */
    @Override
    public Mono<ProductModel> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toModel);
    }
}
