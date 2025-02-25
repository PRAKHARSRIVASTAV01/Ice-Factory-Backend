package com.application.Repository;

import com.application.Object.order_status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface order_statusRepository  extends JpaRepository<order_status, Long> {
    List<order_status> findAllByStatus(String status);
}
