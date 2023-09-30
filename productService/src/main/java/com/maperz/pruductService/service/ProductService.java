package com.maperz.pruductService.service;

import com.maperz.pruductService.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    void addProduct(ProductDTO request);

    List<ProductDTO> getAllProducts();

    ProductDTO getProduct(String name);

    void deleteProduct(String name);

}
