package com.arka.microservice.productos.infraestructure.driver.rest.controller;

import com.arka.microservice.productos.domain.models.CategoryModel;
import com.arka.microservice.productos.domain.ports.in.ICategoryPortUseCase;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.req.CategoryRequestDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.resp.CategoryResponseDto;
import com.arka.microservice.productos.infraestructure.driver.rest.mapper.ICategoryMapperDto;
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

    /**
     * Endpoint para obtener todas los objetos.
     * @return Un Flux de objetos como DTO.
     */
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Flux<CategoryResponseDto> getAllCategories(){
        return service.getAllCategories()
                .map(mapper::modelToResponse);
    }

    /**
     * Endpoint para crear un nuevo Objeto.
     * La validación de datos (por ejemplo, @Valid) se realiza en el DTO recibido.
     * @param req Datos para construir el objeto.
     * @return el objeto creado en forma de DTO.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('admin')")
    public Mono<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto req){
        CategoryModel model = mapper.requestToModel(req);
        return service.createCategory(model)
                .map(mapper::modelToResponse);
    }

    /**
     * Endpoint para actualizar un objeto.
     * @param id ID del objeto a actualizar.
     * @param req Datos del objeto actualizados.
     * @return objeto actualizado.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('admin')")
    public Mono<CategoryResponseDto> updateCategory(@PathVariable("id") Long id,
                                                    @Valid @RequestBody CategoryRequestDto req){
        CategoryModel model = mapper.requestToModel(req);
        return service.updateCategory(model, id)
                .map(mapper::modelToResponse);
    }

    /**
     * Endpoint para eliminar un objeto por su ID.
     * @param id ID del objeto a eliminar.
     * @return Respuesta 204 si se elimina correctamente.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('admin')")
    public Mono<Void> deleteCategoryById(@PathVariable("id")Long id){
        return service.deleteCategory(id);
    }

}
