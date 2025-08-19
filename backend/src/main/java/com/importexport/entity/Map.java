package com.importexport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "maps")
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Map name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Map type is required")
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "resolution")
    private Double resolution;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "origin_x")
    private Double originX;

    @Column(name = "origin_y")
    private Double originY;

    @NotNull(message = "Active status is required")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "description")
    private String description;

    // Constructors
    public Map() {}

    public Map(String name, String type, Double resolution, Integer width, Integer height, Double originX, Double originY, Boolean isActive, String description) {
        this.name = name;
        this.type = type;
        this.resolution = resolution;
        this.width = width;
        this.height = height;
        this.originX = originX;
        this.originY = originY;
        this.isActive = isActive;
        this.description = description;
    }

    // Getters and Setters
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getResolution() {
        return resolution;
    }

    public void setResolution(Double resolution) {
        this.resolution = resolution;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getOriginX() {
        return originX;
    }

    public void setOriginX(Double originX) {
        this.originX = originX;
    }

    public Double getOriginY() {
        return originY;
    }

    public void setOriginY(Double originY) {
        this.originY = originY;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}