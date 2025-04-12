package com.application.Service;

import com.application.Object.order_status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.Repository.orderRepository;
import com.application.Object.order;
import com.application.Repository.order_statusRepository;

import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import com.application.Object.OrderHistoryDTO;
import com.application.Object.OrderDetailDTO;
import com.application.Object.user;
import com.application.Object.user_address;
import com.application.Object.address;
import com.application.Repository.user_addressRepository;
import com.application.Repository.addressRepository;
@Service
public class orderServicce {

    @Autowired
    private orderRepository orderRepository;

    @Autowired
    private order_statusService order_statusService;

    @Autowired
    private userService userService;

    @Autowired
    private user_addressRepository userAddressRepository;

    @Autowired
    private addressService addressService;

    @Autowired
    private addressRepository addressRepository;

    public order addOrder(order newOrder) {
        newOrder.setOderDate(new Date());
        newOrder.setOderTime(new Time(System.currentTimeMillis()));
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
                .filter(order -> order.getPhone().equals(phone))
                .collect(Collectors.toList());
    }

    public List<order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Map<String, Integer> getFutureForecast(int days) {
        // Get current date
        LocalDate today = LocalDate.now();
        
        // Create a sorted map to store the forecast (date -> quantity)
        Map<String, Integer> forecast = new TreeMap<>();
        
        // Initialize all dates with zero
        for (int i = 0; i < days; i++) {
            LocalDate date = today.plusDays(i);
            forecast.put(date.toString(), 0);
        }
        
        // Get all future orders with status "placed" or "processing"
        Date startDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(today.plusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<order> futureOrders = orderRepository.findFutureOrdersForForecast(startDate, endDate);
        
        // Process each order
        for (order o : futureOrders) {
            // Convert delivery date to LocalDate
            LocalDate deliveryDate = o.getDeliveryDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
            
            // Add the quantity to the respective date
            String dateKey = deliveryDate.toString();
            if (forecast.containsKey(dateKey)) {
                forecast.put(dateKey, forecast.get(dateKey) + o.getQuantity());
            }
        }
        
        return forecast;
    }

    public List<OrderHistoryDTO> getUserOrderHistory(String phone) {
        List<order> orders = orderRepository.findOrdersByPhoneOrderByDateDesc(phone);
        List<OrderHistoryDTO> orderHistory = new ArrayList<>();
        
        for (order order : orders) {
            String status = order_statusService.getStatus(order.getId());
            orderHistory.add(new OrderHistoryDTO(order, status));
        }
        
        return orderHistory;
    }

    public List<OrderDetailDTO> getDetailedOrdersByDateCriteria(Date orderDate, Date deliveryDate) {
        List<order> orders;
        
        if (orderDate != null && deliveryDate != null) {
            orders = orderRepository.findOrdersByBothDates(orderDate, deliveryDate);
        } else if (orderDate != null) {
            orders = orderRepository.findOrdersByOrderDateOnly(orderDate);
        } else {
            orders = orderRepository.findOrdersByDeliveryDateOnly(deliveryDate);
        }
        
        List<OrderDetailDTO> detailedOrders = new ArrayList<>();
        
        // For each order, get related information
        for (order order : orders) {
            OrderDetailDTO detailDTO = new OrderDetailDTO();
            
            try {
                // Set order data
                detailDTO.setOrderId(order.getId());
                detailDTO.setQuantity(order.getQuantity());
                detailDTO.setOrderDate(order.getOderDate());
                detailDTO.setOrderTime(order.getOderTime());
                detailDTO.setDeliveryDate(order.getDeliveryDate());
                detailDTO.setTotalAmount(order.getTotalAmount());
                
                // Get order status - handle potential null
                try {
                    String status = order_statusService.getStatus(order.getId());
                    detailDTO.setStatus(status != null ? status : "Unknown");
                } catch (Exception e) {
                    detailDTO.setStatus("Unknown");
                }
                
                // Get user data - handle potential null
                String phone = order.getPhone();
                detailDTO.setPhone(phone != null ? phone : "");
                
                try {
                    if (phone != null) {
                        user user = userService.getUserByPhone(phone);
                        if (user != null) {
                            detailDTO.setFirstName(user.getFirstName());
                            detailDTO.setLastName(user.getLastName());
                        }
                    }
                } catch (Exception e) {
                    // If user data can't be retrieved, continue without it
                    detailDTO.setFirstName("");
                    detailDTO.setLastName("");
                }
                
                // Get user address - handle potential null
                try {
                    if (phone != null) {
                        user_address userAddress = userAddressRepository.findByPhone(phone);
                        if (userAddress != null) {
                            Long addressId = userAddress.getAddress_id();
                            detailDTO.setAddressId(addressId);
                            
                            if (addressId != null) {
                                address userAddressDetails = addressRepository.findById(addressId).orElse(null);
                                if (userAddressDetails != null) {
                                    detailDTO.setStreet(userAddressDetails.getStreet());
                                    detailDTO.setCity(userAddressDetails.getCity());
                                    detailDTO.setPincode(userAddressDetails.getPincode());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    // If address data can't be retrieved, continue without it
                }
                
                detailedOrders.add(detailDTO);
            } catch (Exception e) {
                // Log the error and continue with the next order
                System.err.println("Error processing order " + order.getId() + ": " + e.getMessage());
            }
        }
        
        return detailedOrders;
    }

}
