package com.application.Object;

import jakarta.persistence.*;
import jdk.jfr.Unsigned;

@Entity
public class oredr_status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumns({
            @JoinColumn(name = "order_id", referencedColumnName = "order_id"),
    })
    private int id;
    private Boolean status;
}
