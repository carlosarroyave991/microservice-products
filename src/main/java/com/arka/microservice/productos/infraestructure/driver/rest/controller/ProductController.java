package com.arka.microservice.productos.infraestructure.driver.rest.controller;

import com.arka.microservice.productos.domain.models.ProductModel;
import com.arka.microservice.productos.domain.ports.in.IProductPortUseCase;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.repository.IProductCategoryRepository;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.req.ProductRequestDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.req.StockUpdateRequestDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.resp.ProductListResponseDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.resp.ProductResponseDto;
import com.arka.microservice.productos.infraestructure.driver.rest.mapper.IProductMapperDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
@Tag(name = "Product Controller", description = "Endpoints para la gestion de productos")
public class ProductController {
    private final IProductPortUseCase serviceP;
    private final IProductPortUseCase servicePC;
    private final IProductMapperDto mapper;

    /**
     * Endpoint para obtener todas los productos.
     * @return Un Flux de productos como DTO.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<ProductResponseDto> getAllProducts(){
        return serviceP.getAllProducts()
                .map(mapper::modelToResponse);
    }

    /**
     * Endpoint para obtener un usuario por su ID.
     * @param id ID del usuario a consultar.
     * @return Usuario encontrado o respuesta 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductListResponseDto> getProductsById(@PathVariable("id")Long id){
        return serviceP.findByProductId(id)
                .map(mapper::modelsToResponseList);
    }

    /**
     * Endpoint para crear un product
     * @param requestDto data que tendra el prudcto
     * @return product creado en forma de dto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        log.info("Recibido request para crear producto: {}", requestDto);
        // Convertir el DTO a un modelo de dominio.
        ProductModel model = mapper.requestToModel(requestDto);
        log.info("Modelo convertido desde DTO: {}", model);
        return serviceP.createProduct(model)
                .doOnNext(producto -> log.info("Producto creado con éxito en el servicio: {}", producto))
                .map(mapper::modelToResponse)
                .doOnNext(response -> log.info("Respuesta generada para el producto: {}", response))
                .doOnError(error -> log.error("Se presentó un error al crear el producto", error));
    }

    /**
     * Endpoint para eliminar una dirección por su ID
     * @return Un Mono vacío que indica que la operación se completó.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(@PathVariable("id")Long id){
        return serviceP.deleteProduct(id);
    }

    /**
     * Endpoint para actualizar un objeto.
     * @param id ID del objeto a actualizar.
     * @param req Datos del objeto actualizados.
     * @return objeto actualizado.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductResponseDto> updateProduct(@PathVariable("id")Long id,
                                                  @Valid @RequestBody ProductRequestDto req){
        ProductModel model = mapper.requestToModel(req);
        return serviceP.updateProduct(model, id)
                .map(mapper::modelToResponse);
    }

    /**
     * Endpoint para actualizar el stock
     * @param productId identificador del producto
     * @param request objeto donde viene la cantidad a sumar
     * @return un objeto mono o un mono error
     */
    @PutMapping("/{id}/stock")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> updateStock(@PathVariable("id")Long productId,
                                  @RequestBody StockUpdateRequestDto request){
        return serviceP.updateStock(productId, request.getQuantity());
    }
}
