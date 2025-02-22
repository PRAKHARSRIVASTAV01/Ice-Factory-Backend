package com.application.Object;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class address {
    @Id
    private int address_id;
    private String street;
    private String city;
    private String pincode;
}
