package com.maperz.inventoryService.service;

import com.maperz.inventoryService.dto.InventoryDTO;
import com.maperz.inventoryService.model.Inventory;
import com.maperz.inventoryService.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository repository;

    public List<InventoryDTO> isInStock(List<String> skuCode) {
        if (skuCode.contains("all")) {
            return getAllInventories();
        }

        return repository.findBySkuCodeIn(skuCode)
                .stream()
                .map(inventory -> InventoryDTO.builder()
                        .skuCode(inventory.getSkuCode())
                        .quantity(inventory.getQuantity())
                        .inStock(inventory.getQuantity() > 0)
                        .build())
                .toList();
    }

    public void createInventory(InventoryDTO request) {
        if (repository.existsBySkuCode(request.getSkuCode())) {
            throw new RuntimeException("Inventory already exists");
        }
        Inventory inventory = new Inventory();
        inventory.setSkuCode(request.getSkuCode());
        inventory.setQuantity(request.getQuantity());
        repository.save(inventory);
    }


    public void updateInventory(Map<String, Integer> requests, String mode) {
        if (!mode.equals("add") && !mode.equals("remove")) {
            throw new RuntimeException("Mode must be add or remove");
        }

        requests.forEach((skuCode, quantity) -> {

            Inventory inventory = findInventory(skuCode);
            if (quantity > inventory.getQuantity() && mode.equals("remove")) {
                throw new RuntimeException("Not enough inventory");
            } else if (quantity < 0) {
                throw new RuntimeException("Quantity cannot be less than 0");
            } else {
                inventory.setQuantity(
                        mode.equals("add") ?
                                inventory.getQuantity() + quantity :
                                inventory.getQuantity() - quantity);
                log.info("Updating inventory for skuCode {} with quantity {}", skuCode, quantity);
                repository.save(inventory);
            }
        });
    }

    public void deleteInventory(String skuCode) {
        Inventory inventory = findInventory(skuCode);
        repository.delete(inventory);
    }

    private Inventory findInventory(String skuCode) {
        return repository.findBySkuCode(skuCode)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    private List<InventoryDTO> getAllInventories() {
        return repository.findAll()
                .stream().map(inventory -> InventoryDTO.builder()
                        .skuCode(inventory.getSkuCode())
                        .quantity(inventory.getQuantity())
                        .inStock(inventory.getQuantity() > 0)
                        .build()).toList();
    }
}
