package com.application.Service;

import com.application.Object.order;
import com.application.Object.order_status;
import com.application.Object.sales_details;
import com.application.Repository.order_statusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class order_statusService {
    @Autowired
    private order_statusRepository order_statusRepository;

    @Autowired
    private com.application.Repository.orderRepository orderRepository;

    @Autowired
    private com.application.Repository.sales_detailsRepository salesDetailsRepository;
    
    @Autowired
    private notificationService notificationService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addStatus(Long id) {
        try {
            // Add synchronization to prevent concurrent access
            synchronized (this) {
                // First check if a status already exists for this order - use for update to lock the row
                boolean statusExists = order_statusRepository.existsById(id);
                
                if (statusExists) {
                    System.out.println("Status already exists for order #" + id);
                    return; // Status already exists, don't create a duplicate
                }

                // Create and save new status
                order_status newStatus = new order_status();
                newStatus.setId(id);
                newStatus.setStatus("pending"); // Use your default initial status
                System.out.println("Creating new status for order #" + id + ": pending");
                order_statusRepository.saveAndFlush(newStatus); // Use saveAndFlush to commit immediately
                
                // Send notification (if applicable)
                order order = orderRepository.findById(id).orElse(null);
                if (order != null) {
                    notificationService.sendOrderPlacedNotification(order);
                    System.out.println("Notification sent for order #" + id);
                }
            }
        } catch (Exception e) {
            System.err.println("Error adding order status: " + e.getMessage());
            e.printStackTrace();
            // Don't rethrow to prevent transaction rollback
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
