package com.maperz.inventoryService.controller;


import com.maperz.inventoryService.dto.InventoryDTO;
import com.maperz.inventoryService.service.impl.InventoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryServiceImpl service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryDTO> checkInventoryWithParams(@RequestParam List<String> skuCode){
        return service.isInStock(skuCode);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createInventory(@RequestBody InventoryDTO request){
        service.createInventory(request);
    }

    @PutMapping("/{mode}")
    @ResponseStatus(HttpStatus.OK)
    public void updateInventory(@RequestBody @Validated Map<String, Integer> requests, @PathVariable String mode){
        service.updateInventory(requests, mode);
    }

    @DeleteMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteInventory(@PathVariable String skuCode){
        service.deleteInventory(skuCode);
    }



}
