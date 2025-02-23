package com.application.Object;

import jakarta.persistence.*;

@Entity
public class user_credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumns({
            @JoinColumn(name = "user_phone", referencedColumnName = "phone"),
    })
    private String phone;

    private String password;

    private Boolean is_registered;
}
