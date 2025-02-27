package com.application.Object;

import jakarta.persistence.*;

import java.sql.Time;
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

    public user getPhone() {
        return phone;
    }

    public void setPhone(user phone) {
        this.phone = phone;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public Date getOderDate() {
        return oderDate;
    }

    public void setOderDate(Date oderDate) {
        this.oderDate = oderDate;
    }

    public Time getOderTime() {
        return oderTime;
    }

    public void setOderTime(Time oderTime) {
        this.oderTime = oderTime;
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

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }
}
