package com.application.Object;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class sales {
    @Id
    private Long id;
    private float totalOrders;
    private float totalSales;
    private float totalQuantity;
    @JoinColumns({
            @JoinColumn(name = "order_deliveryDate", referencedColumnName = "deliveryDate"),
    })
    private Date deliveryDate;
}
