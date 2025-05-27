package com.arka.microservice.productos.infraestructure.driven.r2dbc.mapper;

import com.arka.microservice.productos.domain.models.CategoryModel;
import com.arka.microservice.productos.infraestructure.driven.r2dbc.entity.CategoryEntity;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ICategoryEntityMapper {
    CategoryEntity toEntity(CategoryModel model);

    @InheritInverseConfiguration
    CategoryModel toModel(CategoryEntity entity);

    // Método para convertir de LocalDate a Date (para convertir desde tu modelo a entidad)
    default Date localDateToDate(LocalDate localDate) {
        return localDate == null
                ? null
                : Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Método para convertir de Date a LocalDate (para convertir desde tu entidad a modelo)
    default LocalDate dateToLocalDate(Date date) {
        return date == null
                ? null
                : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
