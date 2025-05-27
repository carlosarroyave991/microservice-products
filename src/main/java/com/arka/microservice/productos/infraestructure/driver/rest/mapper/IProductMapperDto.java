package com.arka.microservice.productos.infraestructure.driver.rest.mapper;

import com.arka.microservice.productos.domain.models.ProductCategoryModel;
import com.arka.microservice.productos.domain.models.ProductListModel;
import com.arka.microservice.productos.domain.models.ProductModel;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.req.ProductRequestDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.resp.ProductCategoryListResponseDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.resp.ProductListResponseDto;
import com.arka.microservice.productos.infraestructure.driver.rest.dto.resp.ProductResponseDto;
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
public interface IProductMapperDto {
    ProductModel requestToModel(ProductRequestDto req);

    ProductResponseDto modelToResponse(ProductModel model);

    ProductCategoryListResponseDto modelsToResponse(ProductModel models);

    ProductListResponseDto modelsToResponseList(ProductListModel listModel);
}
