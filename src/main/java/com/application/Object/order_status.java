package com.application.Object;

import jakarta.persistence.*;
import jdk.jfr.Unsigned;

@Entity
public class order_status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumns({
            @JoinColumn(name = "order_id", referencedColumnName = "order_id"),
    })
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
