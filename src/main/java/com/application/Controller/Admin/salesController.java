package com.application.Controller.Admin;

import com.application.Object.sales_details;
import com.application.Object.sales_report;
import com.application.Service.salesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("api/admin/sales")
public class salesController {

    @Autowired
    private com.application.Service.salesService salesService;

    @GetMapping("/salesDetails")
       public ResponseEntity<List<sales_details>> getAllSalesDetails() {
        List<sales_details> salesDetails = salesService.getAllSalesDetails();
        return new ResponseEntity<>(salesDetails, HttpStatus.OK);
    }


    @GetMapping("/salesDetails/{id}")
    public ResponseEntity<sales_details> getSalesDetailsById(@PathVariable Long id) {
        try {
            sales_details salesDetails = salesService.getSalesDetailsById(id);
            return new ResponseEntity<>(salesDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/salesDetails")
    public ResponseEntity<sales_details> createSalesDetails(@RequestBody sales_details salesDetails) {
        try {
            sales_details createdSalesDetails = salesService.createSalesDetails(salesDetails);
            return new ResponseEntity<>(createdSalesDetails, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/salesDetails/{id}")
    public ResponseEntity<sales_details> updateSalesDetails(@PathVariable Long id, @RequestBody sales_details salesDetails) {
        try {
            sales_details updatedSalesDetails = salesService.updateSalesDetails(id, salesDetails);
            return new ResponseEntity<>(updatedSalesDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/salesDetails/{id}")
    public ResponseEntity<Boolean> deleteSalesDetails(@PathVariable Long id) {
        try {
            boolean result = salesService.deleteSalesDetails(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/salesReports")
    public ResponseEntity<List<sales_report>> getAllSalesReports() {
        try {
            List<sales_report> salesReports = salesService.getAllSalesReports();
            return new ResponseEntity<>(salesReports, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/salesReports/{date}")
    public ResponseEntity<sales_report> getSalesReportByDate(@PathVariable Date date) {
        try {
            sales_report salesReport = salesService.getSalesReportByDate(date);
            return new ResponseEntity<>(salesReport, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/salesReports")
    public ResponseEntity<sales_report> createSalesReport(@RequestBody sales_report salesReport) {
        try {
            sales_report createdSalesReport = salesService.createSalesReport(salesReport);
            return new ResponseEntity<>(createdSalesReport, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/salesReports/{date}")
    public ResponseEntity<sales_report> updateSalesReport(@PathVariable Date date, @RequestBody sales_report salesReport) {
        try {
            sales_report updatedSalesReport = salesService.updateSalesReport(date, salesReport);
            return new ResponseEntity<>(updatedSalesReport, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/salesReports/{date}")
    public ResponseEntity<Boolean> deleteSalesReport(@PathVariable Date date) {
        try {
            boolean result = salesService.deleteSalesReport(date);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
