package com.maperz.pruductService.repository;

import com.maperz.pruductService.dto.ProductResponse;
import com.maperz.pruductService.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsByName(String name);

    Product findByName(String name);

}
