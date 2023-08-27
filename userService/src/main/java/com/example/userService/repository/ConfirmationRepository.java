package com.example.userService.repository;

import com.example.userService.model.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Integer> {

    Optional<Confirmation> findByToken(String token);
}
