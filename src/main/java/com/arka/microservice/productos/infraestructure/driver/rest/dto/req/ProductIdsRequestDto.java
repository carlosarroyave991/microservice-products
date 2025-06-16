package com.arka.microservice.productos.infraestructure.driver.rest.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class ProductIdsRequestDto {
    @NotEmpty(message = "La lista de IDs no puede estar vacía")
    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}