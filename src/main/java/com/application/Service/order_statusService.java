package com.application.Service;

import com.application.Object.order;
import com.application.Object.order_status;
import com.application.Object.sales_details;
import com.application.Repository.order_statusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class order_statusService {
    @Autowired
     order_statusRepository order_statusRepository;

    @Autowired
    private com.application.Repository.orderRepository orderRepository;

    @Autowired
    private com.application.Repository.sales_detailsRepository salesDetailsRepository;
    
    @Autowired
    private notificationService notificationService;

    public void addStatus(Long id) {
        order_status newStatus = new order_status();
        newStatus.setId(id);
        newStatus.setStatus("placed");
        order_statusRepository.save(newStatus);
        
        // Send notification for new order
        order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            notificationService.sendOrderPlacedNotification(order);
        }
    }
    
    public String getStatus(Long id) {
        order_status status = order_statusRepository.findById(id).orElse(null);
        return status != null ? status.getStatus() : null;
    }

    public void updateStatus(Long id, String status) {
        order_status orderStatus = order_statusRepository.findById(id).orElse(null);
        if (orderStatus != null) {
            orderStatus.setStatus(status);
            order_statusRepository.save(orderStatus);

            order order = orderRepository.findById(id).orElse(null);
            if (order != null) {
                // Send notification for status update
                notificationService.sendOrderStatusUpdateNotification(order, status);
                
                if ("delivered".equals(status)) {
                    addOrderToSalesRevenue(id);
                }
            }
        }
    }

    private void addOrderToSalesRevenue(Long orderId) {
        order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            sales_details salesDetails = new sales_details();
            salesDetails.setPhone(order.getPhone());
            salesDetails.setOrderDate(order.getDeliveryDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            salesDetails.setOrder_id(order.getId());
            salesDetails.setQuantity(order.getQuantity());
            salesDetails.setRevenue(order.getTotalAmount()); // Assuming total amount is the revenue
            salesDetailsRepository.save(salesDetails);
        }
    }

    public void deleteStatus(Long id) {
        order_statusRepository.deleteById(id);
    }

    public List<Long> getIdsByStatus(String status) {
        return order_statusRepository.findAllByStatus(status)
                .stream()
                .map(order_status::getId)
                .collect(Collectors.toList());
    }

    public List<order_status> getAllStatuses() {
        return order_statusRepository.findAll();
    }

    public List<order_status> getStatusesByStatus(String status) {
        return order_statusRepository.findAllByStatus(status);
    }


}
