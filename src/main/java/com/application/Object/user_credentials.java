package com.application.Object;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class user_credentials {


    @Id
    private Long id;

    private String phone_number;

    private String password;

    private Boolean is_registered;
}
