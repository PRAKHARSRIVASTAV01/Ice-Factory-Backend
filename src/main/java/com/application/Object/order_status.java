package com.application.Object;

import jakarta.persistence.*;
import jdk.jfr.Unsigned;

@Entity
public class order_status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumns({
            @JoinColumn(name = "order_id", referencedColumnName = "order_id"),
    })
    private Long id;
    private Boolean status;
}
