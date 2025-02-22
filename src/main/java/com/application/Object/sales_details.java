package com.application.Object;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class sales_details {
    @Id
    private Long id;
    private String phone_number;
    private LocalDate orderDate;
    private Long order_id;
    private int quantity;
    private float totalSales;
    private float revenue;
}
