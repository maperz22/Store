package com.maperz.pruductService.repository;

import com.maperz.pruductService.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsByName(String name);

    Product findByName(String name);

}
