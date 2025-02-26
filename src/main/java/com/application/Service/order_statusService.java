package com.application.Service;

import com.application.Object.order_status;
import com.application.Repository.order_statusRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class order_statusService {
     order_statusRepository order_statusRepository;

    public void addStatus(Long id) {
        order_status newStatus = new order_status();
        newStatus.setId(id);
        newStatus.setStatus("placed");
        order_statusRepository.save(newStatus);
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
