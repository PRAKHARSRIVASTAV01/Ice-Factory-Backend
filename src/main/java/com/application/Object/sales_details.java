package com.application.Object;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class sales_details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumns({
            @JoinColumn(name = "user_phone", referencedColumnName = "phone"),
    })
    private String phone;
    private LocalDate orderDate;
    private Long order_id;
    private int quantity;
    private float totalSales;
    private float revenue;
}
