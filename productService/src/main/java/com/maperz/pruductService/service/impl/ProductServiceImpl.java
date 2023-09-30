package com.maperz.pruductService.service.impl;

import com.maperz.pruductService.dto.ProductDTO;
import com.maperz.pruductService.model.Product;
import com.maperz.pruductService.repository.ProductRepository;
import com.maperz.pruductService.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;


    public void addProduct(ProductDTO request) {

        if(!repository.existsByName(request.name())) {
            Product product = Product.builder()
                    .description(request.description())
                    .name(request.name())
                    .price(request.price())
                    .build();

            repository.save(product);
            log.info("Product added successfully");
        } else {
            throw new IllegalArgumentException("Product already exists");
        }
    }


    public List<ProductDTO> getAllProducts() {
        List<Product> products = repository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }


    public ProductDTO getProduct(String name) {
        Product product = repository.findByName(refactorToName(name));
        return mapToProductResponse(product);
    }


    public void deleteProduct(String name) {

        if (repository.existsByName(refactorToName(name))) {
            Product product = repository.findByName(refactorToName(name));
            repository.delete(product);
            log.info("Product Successfully Deleted");
        } else {
            throw new IllegalArgumentException("Product with given name do not exists!");
        }
    }

    private ProductDTO mapToProductResponse(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice());
    }

    private String refactorToName(String toRefactor){
        return toRefactor.replace("-", " ");
    }

}
