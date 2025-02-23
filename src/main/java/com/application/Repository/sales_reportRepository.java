package com.application.Repository;

import com.application.Object.sales_report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface sales_reportRepository  extends JpaRepository<sales_report, Date> {
}
