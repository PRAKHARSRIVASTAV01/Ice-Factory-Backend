package com.application.Service;

import com.application.Object.sales_details;
import com.application.Object.sales_report;
import com.application.Repository.sales_detailsRepository;
import com.application.Repository.sales_reportRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


    public List<sales_report> getSalesReportListByDate(Date date) {
        return salesReportRepository.findAll().stream()
                .filter(salesReport -> salesReport.getDate().equals(date))
                .collect(Collectors.toList());
    }




}
