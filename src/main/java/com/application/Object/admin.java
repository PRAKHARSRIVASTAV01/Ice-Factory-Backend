package com.application.Object;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class admin {
    @Id
    private Long id;
    private String username;
    private String password;

}
