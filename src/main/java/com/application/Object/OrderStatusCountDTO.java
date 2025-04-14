package com.application.Object;

public class OrderStatusCountDTO {
    private String status;
    private Long count;
    private Integer totalQuantity;
    private Float totalRevenue;

    // Constructor for status counts
    public OrderStatusCountDTO(String status, Long count) {
        this.status = status;
        this.count = count;
        this.totalQuantity = 0;
        this.totalRevenue = 0.0f;
    }

    // Full constructor including quantity and revenue
    public OrderStatusCountDTO(String status, Long count, Integer totalQuantity, Float totalRevenue) {
        this.status = status;
        this.count = count;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    // Default constructor
    public OrderStatusCountDTO() {
        this.totalQuantity = 0;
        this.totalRevenue = 0.0f;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Float getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Float totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}