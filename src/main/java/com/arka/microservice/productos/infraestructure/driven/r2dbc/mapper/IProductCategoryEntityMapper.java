package com.arka.microservice.productos.infraestructure.driven.r2dbc.mapper;

import com.arka.microservice.productos.domain.models.ProductCategoryModel;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.entity.ProductCategoryEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IProductCategoryEntityMapper {
    ProductCategoryEntity toEntity(ProductCategoryModel model);

    @InheritInverseConfiguration
    ProductCategoryModel toModel(ProductCategoryEntity entity);
}
