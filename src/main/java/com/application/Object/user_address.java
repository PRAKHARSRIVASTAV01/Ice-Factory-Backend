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
    private String phone;

    @JoinColumns({
            @JoinColumn(name = "address_address_id", referencedColumnName = "address_id"),
    })
    private Long address_id;

    public user_address(String phone, Long address_id) {
        phone = phone;
        this.address_id = address_id;
    }

    public user_address() {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        phone = phone;
    }

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long address_id) {
        this.address_id = address_id;
    }

    public user_address orElse(Object o) {
        return null;
    }
}
