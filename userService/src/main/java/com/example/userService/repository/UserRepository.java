package com.example.userService.repository;

import com.example.userService.model.Order;
import com.example.userService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailIgnoreCase(String email);
    Boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByOrdersIsContaining(Order order);
}
