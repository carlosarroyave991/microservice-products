package com.arka.microservice.productos.infraestructure.driven.r2dbc.adapter;


import com.arka.microservice.productos.domain.models.ProductCategoryModel;
import com.arka.microservice.productos.domain.ports.out.ProductCategoryPersistencePort;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.entity.ProductCategoryEntity;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.mapper.IProductCategoryEntityMapper;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.repository.IProductCategoryRepository;
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
public class ProductCategoryAdapterImpl implements ProductCategoryPersistencePort {
    private final IProductCategoryRepository repository;
    private final IProductCategoryEntityMapper mapper;


    /**Funcion que retorna un product con todas sus categorias
     * @param id identificador del objeto product
     * @return objeto o mono error
     */
    @Override
    public Flux<ProductCategoryModel> findByProductId(Long id) {
        return repository.findByProductId(id)
                .map(mapper::toModel);
    }

    /**
     * Funcion que guarda un objeto
     * @param model objeto a guardar
     * @return retorna un objeto mapeado para el dominio
     */
    @Override
    public Mono<ProductCategoryModel> save(ProductCategoryModel model) {
        ProductCategoryEntity entity = mapper.toEntity(model);
        return repository.save(entity)
                .map(mapper::toModel);
    }
}
