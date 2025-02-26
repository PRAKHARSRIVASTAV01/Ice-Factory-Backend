package com.application.Service;

import com.application.Object.order_status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.Repository.orderRepository;
import com.application.Object.order;
import com.application.Repository.order_statusRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class orderServicce {

    @Autowired
    private orderRepository orderRepository;
    @Autowired
    private order_statusService order_statusService;

    public order addOrder(order newOrder) {
        order savedOrder = orderRepository.save(newOrder);
        order_statusService.addStatus(savedOrder.getId());
        return savedOrder;
    }

    public order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public order updateOrder(Long id, order orderDetails) {
        order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setPhone(orderDetails.getPhone());
            order.setQuantity(orderDetails.getQuantity());
            order.setOderDate(orderDetails.getOderDate());
            order.setOderTime(orderDetails.getOderTime());
            order.setDeliveryDate(orderDetails.getDeliveryDate());
            order.setTotalAmount(orderDetails.getTotalAmount());
            return orderRepository.save(order);
        }
        return null;
    }

    public boolean deleteOrder(Long id) {
        order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<order> getOrdersByDeliveryDate(Date deliveryDate) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getDeliveryDate().equals(deliveryDate))
                .collect(Collectors.toList());
    }

    public List<order> getOrdersByOrderDate(Date orderDate) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOderDate().equals(orderDate))
                .collect(Collectors.toList());
    }

    public List<order> getOrdersByPhone(String phone) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getPhone().getPhone().equals(phone))
                .collect(Collectors.toList());
    }

    public List<order> getAllOrders() {
        return orderRepository.findAll();
    }



}
