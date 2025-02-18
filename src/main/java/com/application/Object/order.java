package com.application.Object;

import jakarta.persistence.*;

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
    private LocalDateTime oderDate;
    private Date deliveryDate;
    private float totalAmount;


}
