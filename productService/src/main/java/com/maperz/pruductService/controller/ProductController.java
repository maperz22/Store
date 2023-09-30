package com.maperz.pruductService.controller;

import com.maperz.pruductService.dto.ProductDTO;
import com.maperz.pruductService.service.ProductService;
import com.maperz.pruductService.service.impl.ProductServiceImpl;
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
    public void addProduct(@RequestBody ProductDTO request){
        service.addProduct(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> getAllProducts(){
        return service.getAllProducts();
    }

    @GetMapping(value = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProduct(@PathVariable String name) {
        return service.getProduct(name);
    }

    @DeleteMapping(value = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String name) {
        service.deleteProduct(name);
    }

}


