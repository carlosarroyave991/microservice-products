package com.arka.microservice.productos.domain.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

/**
 * Entidad product creada para el dominio.
 * La idea es que sea inmutable para garantizar consistencia.
 */
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer stock;
    /*@Column("category_id")
    private Long categoryId;*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
