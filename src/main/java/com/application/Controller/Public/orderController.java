package com.application.Controller.Public;

import com.application.Object.OrderDetailDTO;
import com.application.Object.order;
import com.application.Object.order_status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.application.Object.OrderHistoryDTO;
import org.springframework.format.annotation.DateTimeFormat;


@Controller("order")
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("api/public")
public class orderController {

    @Autowired
    private com.application.Service.orderServicce orderService;

    @Autowired
    private com.application.Service.order_statusService order_statusService;

    @Autowired
    private com.application.Repository.order_statusRepository order_statusRepository;

    @GetMapping("/orders/all")
    public ResponseEntity<List<order>> getAllOrders() {
        try {
            List<order> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<order> getOrderById(@PathVariable Long id) {
        try {
            order order = orderService.getOrderById(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/orders/new")
    public ResponseEntity<order> addOrder(@RequestBody order newOrder) {
        try {
            order createdOrder = orderService.addOrder(newOrder);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<order> updateOrder(@PathVariable Long id, @RequestBody order orderDetails) {
        try {
            order updatedOrder = orderService.updateOrder(id, orderDetails);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable Long id) {
        try {
            boolean result = orderService.deleteOrder(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/orders/deliveryDate/{deliveryDate}")
    public ResponseEntity<List<order>> getOrdersByDeliveryDate(@PathVariable Date deliveryDate) {
        try {
            List<order> orders = orderService.getOrdersByDeliveryDate(deliveryDate);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/orders/phone/{phone}")
    public ResponseEntity<List<order>> getOrdersByPhone(@PathVariable String phone) {
        try {
            List<order> orders = orderService.getOrdersByPhone(phone);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/orderDate/{orderDate}")
    public ResponseEntity<List<order>> getOrdersByOrderDate(@PathVariable Date orderDate) {
        try {
            List<order> orders = orderService.getOrdersByOrderDate(orderDate);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<order>> getOrdersByStatus(@PathVariable String status) {
        try {
            List<Long> orderIds = order_statusService.getIdsByStatus(status);
            List<order> orders = orderIds.stream()
                    .map(orderService::getOrderById)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/orders/status/{id}/{status}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @PathVariable String status) {
        try {
            order_statusService.updateStatus(id, status);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/orders/status/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        try {
            order_statusService.deleteStatus(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/orders/statuses")
    public ResponseEntity<List<order_status>> getAllStatuses() {
        try {
            List<order_status> statuses = order_statusService.getAllStatuses();
            return new ResponseEntity<>(statuses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/statuses/{status}")
    public ResponseEntity<List<order_status>> getStatusesByStatus(@PathVariable String status) {
        try {
            List<order_status> statuses = order_statusService.getStatusesByStatus(status);
            return new ResponseEntity<>(statuses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/forecast/next-15-days")
    public ResponseEntity<Map<String, Integer>> getFutureOrderForecast() {
        try {
            Map<String, Integer> forecast = orderService.getFutureForecast(15);
            return new ResponseEntity<>(forecast, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/history/{phone}")
    public ResponseEntity<List<OrderHistoryDTO>> getUserOrderHistory(@PathVariable String phone) {
        try {
            List<OrderHistoryDTO> orderHistory = orderService.getUserOrderHistory(phone);
            return new ResponseEntity<>(orderHistory, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/detailed")
    public ResponseEntity<List<OrderDetailDTO>> getDetailedOrders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date orderDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date deliveryDate) {
        try {
            if (orderDate == null && deliveryDate == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            List<OrderDetailDTO> detailedOrders = orderService.getDetailedOrdersByDateCriteria(orderDate, deliveryDate);
            return new ResponseEntity<>(detailedOrders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String getStatus(Long id) {
        try {
            order_status status = order_statusRepository.findById(id).orElse(null);
            System.out.println("Looking for status of order #" + id + ", found: " + (status != null ? status.getStatus() : "null"));
            return status != null ? status.getStatus() : "Not Found";
        } catch (Exception e) {
            System.err.println("Error getting status for order #" + id + ": " + e.getMessage());
            e.printStackTrace();
            return "Error";
        }
    }

}
