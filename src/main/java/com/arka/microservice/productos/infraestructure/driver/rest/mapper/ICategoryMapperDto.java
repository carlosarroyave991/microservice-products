package com.arka.microservice.productos.infraestructure.driver.rest.mapper;

import com.arka.microservice.productos.domain.models.CategoryModel;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.req.CategoryRequestDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.resp.CategoryResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ICategoryMapperDto {
    CategoryResponseDto modelToResponse(CategoryModel model);

    CategoryModel requestToModel(CategoryRequestDto req);
}
