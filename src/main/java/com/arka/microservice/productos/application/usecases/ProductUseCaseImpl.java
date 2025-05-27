package com.arka.microservice.productos.application.usecases;

import com.arka.microservice.productos.domain.exception.DuplicateResourceException;
import com.arka.microservice.productos.domain.models.CategoryModel;
import com.arka.microservice.productos.domain.models.ProductCategoryModel;
import com.arka.microservice.productos.domain.models.ProductListModel;
import com.arka.microservice.productos.domain.models.ProductModel;
import com.arka.microservice.productos.domain.ports.in.IProductPortUseCase;
import com.arka.microservice.productos.domain.ports.out.CategoryPersistencePort;
import com.arka.microservice.productos.domain.ports.out.ProductCategoryPersistencePort;
import com.arka.microservice.productos.domain.ports.out.ProductPersistencePort;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.entity.ProductCategoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.arka.microservice.productos.domain.exception.error.CommonErrorCode.DB_EMPTY;
import static com.arka.microservice.productos.domain.exception.error.CommonErrorCode.ID_NOT_FOUND;

/**
 * Clase usada para implementar la logica de negocio sobre cada funcion
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductUseCaseImpl implements IProductPortUseCase {
    private final ProductPersistencePort service;
    private final ProductCategoryPersistencePort servicePC;
    private final CategoryPersistencePort serviceC;

    /**
     * Servicio para obtener un objeto especifico de forma reactiva.
     * @param id parámetro usado para la consulta del objeto.
     * @return retorna un Mono que emite el objeto o error si no se encuentra.
     */
    @Override
    public Mono<ProductListModel> findByProductId(Long id) {
        //buscar el producto
        Mono<ProductModel> productSimple = service.findById(id);

        //Buscar las relaciones de producto con categoria
        Flux<ProductCategoryModel> relationFlux = servicePC.findByProductId(id);
        Mono<List<Long>> categoryIdsMono = relationFlux
                .map(ProductCategoryModel::getCategoryId)
                .collectList();

        // Con los IDs, se buscan las categorías reales
        Mono<List<CategoryModel>> categoriesMono = categoryIdsMono
                .flatMapMany(ids -> serviceC.findAllById(ids))
                .map(category -> new CategoryModel(
                        category.getId(),
                        category.getName(),
                        category.getDescription(),
                        category.getImage(),
                        category.getCreatedDate(),
                        category.getUpdatedDate()
                ))
                .collectList();

        // Se combinan ambos Monos para obtener la respuesta final
        return productSimple.zipWith(categoriesMono, (product, categories) ->
                new ProductListModel(
                        product.getId(),
                        product.getName(),
                        product.getBrand(),
                        product.getPrice(),
                        product.getStock(),
                        categories)
        );
    }

    /**
     * Servicio que permite actualizar un objeto específico de forma reactiva.
     * @param model objeto con los datos a actualizar.
     * @param id   identificador del objeto.
     * @return retorna un Mono con el objeto actualizado o error si no existe.
     */
    @Transactional
    @Override
    public Mono<ProductModel> updateProduct(ProductModel model, Long id) {
        return service.findById(id)
                .switchIfEmpty(Mono.error(new DuplicateResourceException(ID_NOT_FOUND)))
                .flatMap(existing ->{
                    existing.setId(id);
                    if (model.getName() != null)existing.setName(model.getName());
                    if (model.getBrand() != null)existing.setBrand(model.getBrand());
                    if (model.getPrice() != null)existing.setPrice(model.getPrice());
                    if (model.getStock() != null)existing.setStock(model.getStock());

                    return service.update(existing);
                });
    }

    /**
     * Servicio que obtiene todos los objetos existentes de forma reactiva.
     * @return retorna un Flux que emite cada objeto o error si la lista está vacía.
     */
    @Override
    public Flux<ProductModel> getAllProducts() {
        return service.getAll()
                .switchIfEmpty(Flux.error(new DuplicateResourceException(DB_EMPTY)));
    }

    /**
     * Servicio usado para crear un objeto de forma reactiva.
     * @param model objeto con los parámetros necesarios para la creación.
     * @return retorna un Mono con el objeto creado o un error.
     */
    @Transactional
    @Override
    public Mono<ProductModel> createProduct(ProductModel model) {
        return service.save(model);
    }

    /**
     * Servicio para eliminar un objeto de forma reactiva.
     * @param id identificador del objeto a eliminar.
     * @return retorna un Mono<Void> que completa o emite un error si el objeto no existe.
     */
    @Override
    public Mono<Void> deleteProduct(Long id) {
        return service.findById(id)
                .switchIfEmpty(Mono.error(new DuplicateResourceException(ID_NOT_FOUND)))
                .flatMap(existing -> service.deleteById(existing.getId()));
    }
}
