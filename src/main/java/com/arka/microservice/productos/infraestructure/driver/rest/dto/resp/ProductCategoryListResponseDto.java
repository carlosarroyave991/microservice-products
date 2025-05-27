package com.arka.microservice.productos.infraestructure.driver.rest.dto.resp;

import java.math.BigDecimal;
import java.util.List;

public class ProductCategoryListResponseDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer stock;

    List<CategoryResponseDto> categoryResponseDtoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<CategoryResponseDto> getCategoryResponseDtoList() {
        return categoryResponseDtoList;
    }

    public void setCategoryResponseDtoList(List<CategoryResponseDto> categoryResponseDtoList) {
        this.categoryResponseDtoList = categoryResponseDtoList;
    }
}
