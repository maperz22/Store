package com.example.userService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String password;
    private Boolean isActive;
    private Boolean Administrator;

    @OneToOne(cascade = CascadeType.ALL)
    private UserPersonalInfo userPersonalInfo;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Order> orders;

}
