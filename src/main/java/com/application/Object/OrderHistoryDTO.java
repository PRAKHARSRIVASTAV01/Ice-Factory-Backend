package com.application.Object;

import java.sql.Time;
import java.util.Date;

public class OrderHistoryDTO {
    private Long id;
    private int quantity;
    private Date orderDate;
    private Time orderTime;
    private Date deliveryDate;
    private float totalAmount;
    private String status;
    
    public OrderHistoryDTO(order order, String status) {
        this.id = order.getId();
        this.quantity = order.getQuantity();
        this.orderDate = order.getOderDate();
        this.orderTime = order.getOderTime();
        this.deliveryDate = order.getDeliveryDate();
        this.totalAmount = order.getTotalAmount();
        this.status = status;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Date getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    
    public Time getOrderTime() {
        return orderTime;
    }
    
    public void setOrderTime(Time orderTime) {
        this.orderTime = orderTime;
    }
    
    public Date getDeliveryDate() {
        return deliveryDate;
    }
    
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    public float getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}