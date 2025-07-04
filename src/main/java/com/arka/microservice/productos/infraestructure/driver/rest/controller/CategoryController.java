package com.arka.microservice.productos.infraestructure.driver.rest.controller;

import com.arka.microservice.productos.domain.models.CategoryModel;
import com.arka.microservice.productos.domain.ports.in.ICategoryPortUseCase;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.req.CategoryRequestDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.resp.CategoryResponseDto;
import com.arka.microservice.productos.infraestructure.driver.rest.mapper.ICategoryMapperDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
@Tag(name = "Category Controller", description = "Endpoints para la gestion de categorias")
public class CategoryController {
    private final ICategoryPortUseCase service;
    private final ICategoryMapperDto mapper;

    @Operation(summary = "Obtener todas las categorías", description = "Retorna una lista de todas las categorías disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Flux<CategoryResponseDto> getAllCategories(){
        return service.getAllCategories()
                .map(mapper::modelToResponse);
    }

    @Operation(summary = "Crear nueva categoría", description = "Crea una nueva categoría en el sistema (requiere rol de administrador)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - se requiere rol de administrador")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('admin')")
    public Mono<CategoryResponseDto> createCategory(
            @Parameter(description = "Datos de la categoría a crear", required = true) @Valid @RequestBody CategoryRequestDto req){
        CategoryModel model = mapper.requestToModel(req);
        return service.createCategory(model)
                .map(mapper::modelToResponse);
    }

    @Operation(summary = "Actualizar categoría", description = "Actualiza los datos de una categoría existente (requiere rol de administrador)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - se requiere rol de administrador")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('admin')")
    public Mono<CategoryResponseDto> updateCategory(
            @Parameter(description = "ID de la categoría a actualizar", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Datos actualizados de la categoría", required = true) @Valid @RequestBody CategoryRequestDto req){
        CategoryModel model = mapper.requestToModel(req);
        return service.updateCategory(model, id)
                .map(mapper::modelToResponse);
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría del sistema por su ID (requiere rol de administrador)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - se requiere rol de administrador")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('admin')")
    public Mono<Void> deleteCategoryById(
            @Parameter(description = "ID de la categoría a eliminar", required = true) @PathVariable("id")Long id){
        return service.deleteCategory(id);
    }

}
