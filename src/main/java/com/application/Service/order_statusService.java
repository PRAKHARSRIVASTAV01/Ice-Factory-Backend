package com.application.Service;

import com.application.Object.order_status;
import com.application.Repository.order_statusRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.stereotype.Service;

@Service
public class order_statusService {
     order_statusRepository order_statusRepository;

    public void addStatus(Long id) {
        order_status newStatus = new order_status();
        newStatus.setId(id);
        newStatus.setStatus("placed");
        order_statusRepository.save(newStatus);
    }
}
