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
    private String phone; // This is your primary key

    @JoinColumns({
            @JoinColumn(name = "address_address_id", referencedColumnName = "address_id"),
    })
    private Long address_id;

    // The issue is in your constructor - you're assigning phone to itself
    public user_address(String userPhone, Long addressId) {
        this.phone = userPhone; // Fix this line - use this.phone
        this.address_id = addressId;
    }

    public user_address() {
    }

    public String getPhone() {
        return phone;
    }

    // The issue is also in your setter - same problem
    public void setPhone(String userPhone) {
        this.phone = userPhone; // Fix this line - use this.phone
    }

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long address_id) {
        this.address_id = address_id;
    }

    // This method is strange and doesn't make sense for this entity
    // You should remove it as it's not a standard JPA entity method
    public user_address orElse(Object o) {
        return null;
    }
}
