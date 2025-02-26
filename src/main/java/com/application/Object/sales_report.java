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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getTotal_orders() {
        return total_orders;
    }

    public void setTotal_orders(float total_orders) {
        this.total_orders = total_orders;
    }

    public float getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(float total_quantity) {
        this.total_quantity = total_quantity;
    }

    public float getTotal_revenue() {
        return total_revenue;
    }

    public void setTotal_revenue(float total_revenue) {
        this.total_revenue = total_revenue;
    }
}
