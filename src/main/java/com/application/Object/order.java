package com.application.Object;

import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_phone", referencedColumnName = "phone"),
    })
    private user phone;
    private int quantity;
    private String status;
    private Date oderDate;
    private Time oderTime;
    private Date deliveryDate;
    private float totalAmount;
    @JoinColumns({
            @JoinColumn(name = "admin_id", referencedColumnName = "id"),
    })
    private int admin_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
