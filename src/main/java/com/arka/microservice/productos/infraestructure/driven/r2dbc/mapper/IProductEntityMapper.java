package com.arka.microservice.productos.infraestructure.driven.r2dbc.mapper;

import com.arka.microservice.productos.domain.models.ProductModel;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.entity.ProductEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface IProductEntityMapper {
    ProductEntity toEntity(ProductModel model);

    @InheritInverseConfiguration
    ProductModel toModel(ProductEntity entity);
}
