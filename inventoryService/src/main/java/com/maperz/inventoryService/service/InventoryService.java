package com.maperz.inventoryService.service;

import com.maperz.inventoryService.dto.InventoryDTO;

import java.util.List;
import java.util.Map;

public interface InventoryService {

    // Return a list of InventoryDTO objects that are in stock
    List<InventoryDTO> isInStock(List<String> skuCode);

    // Create a new inventory item
    void createInventory(InventoryDTO request);

    // Update inventory item
    void updateInventory(Map<String, Integer> requests, String mode);

    // Delete inventory item
    void deleteInventory(String skuCode);
}
