package com.arka.microservice.productos.infraestructure.driver.rest.controller;

import com.arka.microservice.productos.domain.models.ProductCategoryModel;
import com.arka.microservice.productos.domain.ports.in.IProductCategoryPortUseCase;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.req.ProductCategoryRequestDto;
import com.arka.microservice.productos.infraestructure.driver.rest.mapper.IProductCategoryMapperDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-category")
@Tag(name = "Product Category Controller", description = "Endpoints para la gestion de productos")
public class ProductCategoryController {
    private final IProductCategoryPortUseCase servicePC;
    private final IProductCategoryMapperDto mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<?>> createProductCategory(@Valid @RequestBody ProductCategoryRequestDto requestDto) {
        ProductCategoryModel model = mapper.requestToModel(requestDto);
        return servicePC.createProductCategory(model)
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved));

    }
}
