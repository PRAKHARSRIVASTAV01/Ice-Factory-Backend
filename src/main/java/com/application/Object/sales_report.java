package com.application.Object;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class sales_report {
    @Id
    private Date date;
    private float total_orders;
    private float total_quantity;
    private float total_revenue;
}
