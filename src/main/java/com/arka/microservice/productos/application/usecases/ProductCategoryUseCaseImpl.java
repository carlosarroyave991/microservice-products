package com.arka.microservice.productos.application.usecases;

import com.arka.microservice.productos.domain.exception.DuplicateResourceException;
import com.arka.microservice.productos.domain.models.CategoryModel;
import com.arka.microservice.productos.domain.models.ProductCategoryModel;
import com.arka.microservice.productos.domain.models.ProductModel;
import com.arka.microservice.productos.domain.ports.in.IProductCategoryPortUseCase;
import com.arka.microservice.productos.domain.ports.out.CategoryPersistencePort;
import com.arka.microservice.productos.domain.ports.out.ProductCategoryPersistencePort;
import com.arka.microservice.productos.domain.ports.out.ProductPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static com.arka.microservice.productos.domain.exception.error.CommonErrorCode.ID_NOT_FOUND;

/**
 * Clase usada para implementar la logica de negocio sobre cada funcion
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCategoryUseCaseImpl implements IProductCategoryPortUseCase {
    private final ProductCategoryPersistencePort service;
    private final ProductPersistencePort productService;
    private final CategoryPersistencePort categoryService;

    /**
     * Servicio usado para crear un objeto de forma reactiva.
     * @param model objeto con los parámetros necesarios para la creación.
     * @return retorna un Mono con el objeto creado o un error.
     */
    @Transactional
    @Override
    public Mono<ProductCategoryModel> createProductCategory(ProductCategoryModel model) {
        // Validar que el producto exista
        Mono<ProductModel> productMono = productService.findById(model.getProductId())
                .switchIfEmpty(Mono.error(new DuplicateResourceException(ID_NOT_FOUND)));

        // Validar que la categoría exista
        Mono<CategoryModel> categoryMono = categoryService.findById(model.getCategoryId())
                .switchIfEmpty(Mono.error(new DuplicateResourceException(ID_NOT_FOUND)));

        //Una vez validados, si ambos existen se guarda la relación
        return Mono.zip(productMono, categoryMono)
                .flatMap(tuple -> {
                    // Aquí podríamos usar los resultados si se requieren, por ejemplo:
                    ProductModel product = tuple.getT1();
                    CategoryModel category = tuple.getT2();

                    // Guardamos el registro de la relación y retornamos el modelo guardado.
                    return service.save(model)
                            .doOnSuccess(saved -> log.info("Guardada la relación de producto-categoría: {}", saved));
                });
    }
}
