package com.application.Object;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class user {
    @Id
    private String phone;
    private String firstName;
    private String lastName;
    private float rate;

    public user() {
    }

    public user(String phone, String firstName, String lastName, float rate) {
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rate = rate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
