package com.maperz.pruductService.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductDTO(String id, String name, String description, BigDecimal price) {
}
