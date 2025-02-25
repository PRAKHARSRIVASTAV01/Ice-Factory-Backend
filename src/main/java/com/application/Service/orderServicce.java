package com.application.Service;

import com.application.Object.order_status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.Repository.orderRepository;
import com.application.Object.order;
import com.application.Repository.order_statusRepository;

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
}
