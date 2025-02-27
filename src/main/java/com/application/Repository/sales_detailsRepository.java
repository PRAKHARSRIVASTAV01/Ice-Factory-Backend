package com.application.Repository;

import com.application.Object.sales_details;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface sales_detailsRepository extends JpaRepository<sales_details, Long> {
    List<sales_details> findAllByOrderDate(Date today);
}
