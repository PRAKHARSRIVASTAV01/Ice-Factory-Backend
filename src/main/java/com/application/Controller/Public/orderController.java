package com.application.Controller.Public;

import com.application.Object.order;
import com.application.Object.order_status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller("order")
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("api/public/order")
public class orderController {

    @Autowired
    private com.application.Service.orderServicce orderService;

    @Autowired
    private com.application.Service.order_statusService order_statusService;

    @GetMapping("/orders")
    public List<order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public order getOrderById(Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping("/orders")
    public order addOrder(order newOrder) {
        return orderService.addOrder(newOrder);
    }

    @PutMapping("/orders/{id}")
    public order updateOrder(Long id, order orderDetails) {
        return orderService.updateOrder(id, orderDetails);
    }

    @DeleteMapping("/orders/{id}")
    public boolean deleteOrder(Long id) {
        return orderService.deleteOrder(id);
    }

    @GetMapping("/orders/deliveryDate/{deliveryDate}")
    public List<order> getOrdersByDeliveryDate(Date deliveryDate) {
        return orderService.getOrdersByDeliveryDate(deliveryDate);
    }

    @GetMapping("/orders/phone/{phone}")
    public List<order> getOrdersByPhone(String phone) {
        return orderService.getOrdersByPhone(phone);
    }

    @GetMapping("/orders/orderDate/{orderDate}")
    public List<order> getOrdersByOrderDate(Date orderDate) {
        return orderService.getOrdersByOrderDate(orderDate);
    }

    @GetMapping("/orders/status/{status}")
    public List<order> getOrdersByStatus(String status) {
        List<Long> orderIds = order_statusService.getIdsByStatus(status);
        return orderIds.stream()
                       .map(orderService::getOrderById)
                       .collect(Collectors.toList());
    }

    @PutMapping("/orders/status/{id}/{status}")
    public void updateStatus(Long id, String status) {
        order_statusService.updateStatus(id, status);
    }

    @GetMapping("/orders/status/{id}")
    public String getStatus(Long id) {
        return order_statusService.getStatus(id);
    }

    @DeleteMapping("/orders/status/{id}")
    public void deleteStatus(Long id) {
        order_statusService.deleteStatus(id);
    }

    @GetMapping("/orders/statuses")
    public List<order_status> getAllStatuses() {
        return order_statusService.getAllStatuses();
    }

    @GetMapping("/orders/statuses/{status}")
    public List<order_status> getStatusesByStatus(String status) {
        return order_statusService.getStatusesByStatus(status);
    }




}
