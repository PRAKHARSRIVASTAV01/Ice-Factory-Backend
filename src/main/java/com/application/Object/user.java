package com.application.Object;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class user {
    @Id
    private String phone;
    private String firstName;
    private String lastName;
    private float rate;
}
