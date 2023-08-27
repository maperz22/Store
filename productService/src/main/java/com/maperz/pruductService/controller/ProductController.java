package com.maperz.pruductService.controller;

import com.maperz.pruductService.dto.ProductRequest;
import com.maperz.pruductService.dto.ProductResponse;
import com.maperz.pruductService.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestBody ProductRequest request){
        service.addProduct(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return service.getAllProducts();
    }

    @GetMapping(value = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProduct(@PathVariable String name) {
        return service.getProduct(name);
    }

    @DeleteMapping(value = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String name) {
        service.deleteProduct(name);
    }

}


