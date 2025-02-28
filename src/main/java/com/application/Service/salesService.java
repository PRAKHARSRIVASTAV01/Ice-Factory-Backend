package com.application.Service;

import com.application.Object.sales_details;
import com.application.Object.sales_report;
import com.application.Repository.sales_detailsRepository;
import com.application.Repository.sales_reportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class salesService {

    @Autowired
    private sales_detailsRepository salesDetailsRepository;

    @Autowired
    private sales_reportRepository salesReportRepository;

    public List<sales_details> getAllSalesDetails() {
        return salesDetailsRepository.findAll();
    }

    public sales_details getSalesDetailsById(Long id) {
        return salesDetailsRepository.findById(id).orElse(null);
    }

    public sales_details createSalesDetails(sales_details salesDetails) {
        return salesDetailsRepository.save(salesDetails);
    }

    public sales_details updateSalesDetails(Long id, sales_details salesDetails) {
        if (salesDetailsRepository.existsById(id)) {
            salesDetails.setId(id);
            return salesDetailsRepository.save(salesDetails);
        } else {
            return null;
        }
    }

    public boolean deleteSalesDetails(Long id) {
        sales_details salesDetails = salesDetailsRepository.findById(id).orElse(null);
        if (salesDetails != null) {
            salesDetailsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<sales_report> getAllSalesReports() {
        return salesReportRepository.findAll();
    }

    public sales_report getSalesReportByDate(Date date) {
        return salesReportRepository.findById(date).orElse(null);
    }

    public sales_report createSalesReport(sales_report salesReport) {
        return salesReportRepository.save(salesReport);
    }



    public sales_report updateSalesReport(Date date, sales_report salesReport) {
        if (salesReportRepository.existsById(date)) {
            salesReport.setDate(date);
            return salesReportRepository.save(salesReport);
        } else {
            return null;
        }
    }

    public boolean deleteSalesReport(Date date) {
        sales_report salesReport = salesReportRepository.findById(date).orElse(null);
        if (salesReport != null) {
            salesReportRepository.deleteById(date);
            return true;
        }
        return false;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void calculateDailySalesReport() {
        Date today = new Date();
        List<sales_details> salesDetailsList = salesDetailsRepository.findAllByOrderDate(today);

        float totalOrders = salesDetailsList.size();
        float totalQuantity = salesDetailsList.stream().mapToInt(sales_details::getQuantity).sum();
        float totalRevenue = (float) salesDetailsList.stream().mapToDouble(sales_details::getRevenue).sum();

        sales_report salesReport = new sales_report();
        salesReport.setDate(today);
        salesReport.setTotal_orders(totalOrders);
        salesReport.setTotal_quantity(totalQuantity);
        salesReport.setTotal_revenue(totalRevenue);

        salesReportRepository.save(salesReport);
    }
}
