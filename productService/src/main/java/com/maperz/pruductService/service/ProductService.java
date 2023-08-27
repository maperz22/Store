package com.maperz.pruductService.service;

import com.maperz.pruductService.dto.ProductRequest;
import com.maperz.pruductService.dto.ProductResponse;
import com.maperz.pruductService.model.Product;
import com.maperz.pruductService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    @Transactional
    public void addProduct(ProductRequest request){

        if(!repository.existsByName(request.getName())) {
            Product product = Product.builder()
                    .description(request.getDescription())
                    .name(request.getName())
                    .price(request.getPrice())
                    .build();

            repository.save(product);
            log.info("Product added successfully");
        } else {
            throw new IllegalArgumentException("Product already exists");
        }
    }


    @Transactional
    public List<ProductResponse> getAllProducts() {
        List<Product> products = repository.findAll();
        List<ProductResponse> responses = products.stream().map(this::mapToProductResponse).toList();
        return responses;
    }

    @Transactional
    public ProductResponse getProduct(String name) {
        Product product = repository.findByName(refactorToName(name));
        return mapToProductResponse(product);
    }

    @Transactional
    public void deleteProduct(String name) {

        if (repository.existsByName(refactorToName(name))) {
            Product product = repository.findByName(refactorToName(name));
            repository.delete(product);
            log.info("Product Successfully Deleted");
        } else {
            throw new IllegalArgumentException("Product with given name do not exists!");
        }
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    private String refactorToName(String toRefactor){
        return toRefactor.replace("-", " ");
    }

}
