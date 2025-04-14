package com.application.Object;

import jakarta.persistence.*;

@Entity
@Table(name = "order_status")
public class order_status {
    @Id
    private Long id;
    
    private String status;

    public order_status() {
    }

    public order_status(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
