package com.arka.microservice.productos.domain.ports.in;

import com.arka.microservice.productos.domain.models.ProductListModel;
import com.arka.microservice.productos.domain.models.ProductModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Se definen las operaciones REST que pueden utilizar para interactuar
 * con el nucreo del sistema.
 */
public interface IProductPortUseCase {
    Mono<ProductListModel> findByProductId(Long id);
    Mono<ProductModel> updateProduct(ProductModel model, Long id);
    Flux<ProductModel> getAllProducts();
    Mono<ProductModel> createProduct(ProductModel model);
    Mono<Void> deleteProduct(Long id);
    Mono<Void> updateStock(Long productId, Integer quantity);
}
