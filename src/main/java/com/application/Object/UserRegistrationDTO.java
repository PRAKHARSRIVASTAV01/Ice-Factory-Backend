package com.application.Object;

public class UserRegistrationDTO {
    private user user;
    private address address;
    
    // Getters and setters
    public user getUser() {
        return user;
    }
    
    public void setUser(user user) {
        this.user = user;
    }
    
    public address getAddress() {
        return address;
    }
    
    public void setAddress(address address) {
        this.address = address;
    }
}