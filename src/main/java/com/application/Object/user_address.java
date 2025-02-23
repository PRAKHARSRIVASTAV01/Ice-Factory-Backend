package com.application.Object;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;

@Entity
public class user_address {
    @Id
    @JoinColumns({
            @JoinColumn(name = "user_phone", referencedColumnName = "phone"),
    })
    private String Phone;

    @JoinColumns({
            @JoinColumn(name = "address_address_id", referencedColumnName = "address_id"),
    })
    private int address_id;
}
