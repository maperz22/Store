package com.maperz.inventoryService.dto;

public record InventoryDTO(String skuCode, Integer quantity, boolean inStock) {
}
