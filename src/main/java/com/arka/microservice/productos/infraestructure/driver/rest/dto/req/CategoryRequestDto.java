package com.arka.microservice.productos.infraestructure.driver.rest.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {
    //El id debe ser nulo para una operación de creación
    @Null(message = "The id must be null for a create operation")
    private Long id; // Opcional para 'save', necesario para 'update'
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String image;
    @Null(message = "The date may arrive empty")
    private Date createdDate;
    @Null(message = "The date may arrive empty")
    private Date updatedDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
