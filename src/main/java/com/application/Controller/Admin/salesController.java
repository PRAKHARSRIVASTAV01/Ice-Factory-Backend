package com.application.Controller.Admin;

import com.application.Object.sales_details;
import com.application.Object.sales_report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Date;
import java.util.List;

@Controller
public class salesController {

    @Autowired
    private com.application.Service.salesService salesService;

    @GetMapping("/salesDetails")
    public List<sales_details> getAllSalesDetails() {
        return salesService.getAllSalesDetails();
    }

    @GetMapping("/salesDetails/{id}")
    public sales_details getSalesDetailsById(Long id) {
        return salesService.getSalesDetailsById(id);
    }

    @PostMapping("/salesDetails")
    public sales_details createSalesDetails(sales_details salesDetails) {
        return salesService.createSalesDetails(salesDetails);
    }

    @PutMapping("/salesDetails/{id}")
    public sales_details updateSalesDetails(Long id, sales_details salesDetails) {
        return salesService.updateSalesDetails(id, salesDetails);
    }

    @DeleteMapping("/salesDetails/{id}")
    public boolean deleteSalesDetails(Long id) {
        return salesService.deleteSalesDetails(id);
    }

    @GetMapping("/salesReports")
    public List<sales_report> getAllSalesReports() {
        return salesService.getAllSalesReports();
    }

    @GetMapping("/salesReports/{date}")
    public sales_report getSalesReportByDate(Date date) {
        return salesService.getSalesReportByDate(date);
    }

    @PostMapping("/salesReports")
    public sales_report createSalesReport(sales_report salesReport) {
        return salesService.createSalesReport(salesReport);
    }

    @PutMapping("/salesReports/{date}")
    public sales_report updateSalesReport(Date date, sales_report salesReport) {
        return salesService.updateSalesReport(date, salesReport);
    }

    @DeleteMapping("/salesReports/{date}")
    public boolean deleteSalesReport(Date date) {
        return salesService.deleteSalesReport(date);
    }



}
