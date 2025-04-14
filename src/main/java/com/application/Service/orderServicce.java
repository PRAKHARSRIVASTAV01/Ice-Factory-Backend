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
import java.util.stream.Collectors;

import com.application.Object.OrderHistoryDTO;
import com.application.Object.OrderDetailDTO;
import com.application.Object.user;
import com.application.Object.user_address;
import com.application.Object.address;
import com.application.Repository.user_addressRepository;
import com.application.Repository.addressRepository;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public order addOrder(order newOrder) {
        try {
            // Set current date and time
            newOrder.setOderDate(new Date());
            newOrder.setOderTime(new Time(System.currentTimeMillis()));
            
            // Make sure required fields are set
            if (newOrder.getPhone() == null || newOrder.getPhone().isEmpty()) {
                throw new IllegalArgumentException("Phone number is required");
            }
            
            if (newOrder.getDeliveryDate() == null) {
                throw new IllegalArgumentException("Delivery date is required");
            }
            
            if (newOrder.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }
            
            // Calculate total amount if not set
            if (newOrder.getTotalAmount() <= 0) {
                // Get user rate or use default rate
                float rate = 10.0f; // Default rate
                user user = userService.getUserByPhone(newOrder.getPhone());
                if (user != null && user.getRate() > 0) {
                    rate = user.getRate();
                }
                
                float totalAmount = newOrder.getQuantity() * rate;
                newOrder.setTotalAmount(totalAmount);
            }
            
            System.out.println("Saving order with phone: " + newOrder.getPhone());
            
            // Save order first
            order savedOrder = orderRepository.saveAndFlush(newOrder);
            System.out.println("Order saved with ID: " + savedOrder.getId());
            
            // Create status in a separate transaction
            order_statusService.addStatus(savedOrder.getId());
            
            return savedOrder;
        } catch (Exception e) {
            System.err.println("Error in addOrder: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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
        // First get all orders for this phone number
        List<order> orders = orderRepository.findOrdersByPhoneOrderByDateDesc(phone);
        
        // Then manually sort them by delivery date in descending order
        orders.sort((a, b) -> b.getDeliveryDate().compareTo(a.getDeliveryDate()));
        
        // Now convert to DTOs
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

    public List<OrderDetailDTO> getOrdersByDeliveryDateRange(Date startDate, Date endDate) {
        List<order> orders = orderRepository.findOrdersByDeliveryDateRange(startDate, endDate);
        List<OrderDetailDTO> detailedOrders = new ArrayList<>();
        
        // For each order, get related information - reusing the code from getDetailedOrdersByDateCriteria
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
                
                // Get order status
                try {
                    String status = order_statusService.getStatus(order.getId());
                    detailDTO.setStatus(status != null ? status : "Unknown");
                } catch (Exception e) {
                    detailDTO.setStatus("Unknown");
                }
                
                // Get user data
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
                
                // Get user address
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

    public Map<String, Object> getOrderStatusCountByDeliveryDate(Date deliveryDate) {
        // Get status counts
        List<Object[]> results = orderRepository.countOrdersByStatusForDeliveryDate(deliveryDate);
        
        Map<String, Long> statusCountMap = new HashMap<>();
        
        // Initialize with all possible statuses set to 0
                statusCountMap.put("confirmed", 0L);
        statusCountMap.put("pending", 0L);
        statusCountMap.put("rejected", 0L);
        statusCountMap.put("delivered", 0L);
        statusCountMap.put("cancelled", 0L);

        statusCountMap.put("pending", 0L);
        
        // Update with actual counts
        for (Object[] result : results) {
            String status = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            statusCountMap.put(status, count);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("statusCounts", statusCountMap);
        
        // Initialize default values
        Integer totalQuantity = 0;
        Float totalRevenue = 0.0f;
        
        try {
            // Use the new separate methods for metrics
            Integer quantity = orderRepository.getTotalDeliveredQuantityForDate(deliveryDate);
            System.out.println("Delivered quantity for " + deliveryDate + ": " + quantity);
            if (quantity != null) {
                totalQuantity = quantity;
            }
            
            Float revenue = orderRepository.getTotalDeliveredRevenueForDate(deliveryDate);
            System.out.println("Delivered revenue for " + deliveryDate + ": " + revenue);
            if (revenue != null) {
                totalRevenue = revenue;
            }
        } catch (Exception e) {
            System.err.println("Error processing delivered metrics: " + e.getMessage());
            e.printStackTrace();
        }
        
        response.put("totalQuantity", totalQuantity);
        response.put("totalRevenue", totalRevenue);
        
        try {
            // Get count of pending future orders
            Long pendingFutureOrdersCount = orderRepository.countPendingFutureOrders(deliveryDate);
            response.put("pendingFutureOrders", pendingFutureOrdersCount != null ? pendingFutureOrdersCount : 0);
        } catch (Exception e) {
            System.err.println("Error getting pending future orders: " + e.getMessage());
            e.printStackTrace();
            response.put("pendingFutureOrders", 0);
        }
        
        return response;
    }

}
