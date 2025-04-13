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

        public user_credentials(Long id, Boolean is_registered, String password, String phone) {
        this.id = id;
        this.is_registered = is_registered;
        this.password = password;
    }

    public user_credentials() {
    }
    public user_credentials(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIs_registered() {
        return is_registered;
    }

    public void setIs_registered(Boolean is_registered) {
        this.is_registered = is_registered;
    }
}
