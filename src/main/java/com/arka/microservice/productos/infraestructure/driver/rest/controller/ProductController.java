package com.arka.microservice.productos.infraestructure.driver.rest.controller;

import com.arka.microservice.productos.domain.models.ProductModel;
import com.arka.microservice.productos.domain.ports.in.IProductPortUseCase;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.req.ProductIdsRequestDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.req.ProductRequestDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.req.StockUpdateRequestDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.resp.ProductListResponseDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.resp.ProductResponseDto;
import com.arka.microservice.productos.infraestructure.driver.rest.mapper.IProductMapperDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<ProductResponseDto> getAllProducts(){
        return serviceP.getAllProducts()
                .map(mapper::modelToResponse);
    }

    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto específico basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductListResponseDto> getProductsById(
            @Parameter(description = "ID del producto a consultar", required = true) @PathVariable("id")Long id){
        return serviceP.findByProductId(id)
                .map(mapper::modelsToResponseList);
    }

    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductResponseDto> createProduct(
            @Parameter(description = "Datos del producto a crear", required = true) @Valid @RequestBody ProductRequestDto requestDto) {
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

    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(
            @Parameter(description = "ID del producto a eliminar", required = true) @PathVariable("id")Long id){
        return serviceP.deleteProduct(id);
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductResponseDto> updateProduct(
            @Parameter(description = "ID del producto a actualizar", required = true) @PathVariable("id")Long id,
            @Parameter(description = "Datos actualizados del producto", required = true) @Valid @RequestBody ProductRequestDto req){
        ProductModel model = mapper.requestToModel(req);
        return serviceP.updateProduct(model, id)
                .map(mapper::modelToResponse);
    }

    @Operation(summary = "Actualizar stock del producto", description = "Actualiza la cantidad en stock de un producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}/stock")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> updateStock(
            @Parameter(description = "ID del producto", required = true) @PathVariable("id")Long productId,
            @Parameter(description = "Cantidad a actualizar en el stock", required = true) @RequestBody StockUpdateRequestDto request){
        return serviceP.updateStock(productId, request.getQuantity());
    }
    
    @Operation(summary = "Obtener productos por lista de IDs", description = "Retorna múltiples productos basados en una lista de IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente")
    })
    @PostMapping("/by-ids")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ProductResponseDto> getProductsByIds(
            @Parameter(description = "Lista de IDs de productos", required = true) @Valid @RequestBody ProductIdsRequestDto request) {
        return serviceP.getAllByIds(request.getIds())
                .map(mapper::modelToResponse);
    }
}
