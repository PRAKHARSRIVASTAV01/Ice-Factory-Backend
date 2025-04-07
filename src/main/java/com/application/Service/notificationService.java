package com.application.Service;

import com.application.Object.notification;
import com.application.Object.order;
import com.application.Repository.notificationRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class notificationService {

    @Autowired
    private notificationRepository notificationRepository;
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    @Value("${sms.gateway.url}")
    private String smsGatewayUrl;
    
    @Value("${sms.gateway.apiKey}")
    private String smsApiKey;
    
    @Value("${sms.gateway.sender}")
    private String smsSender;
    
    @Value("${sms.enabled}")
    private boolean smsEnabled;
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    public notification createNotification(String recipient, String message, String notificationType) {
        notification newNotification = new notification();
        newNotification.setRecipient(recipient);
        newNotification.setMessage(message);
        newNotification.setNotificationType(notificationType);
        newNotification.setStatus("PENDING");
        newNotification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(newNotification);
    }
    
    public void sendOrderPlacedNotification(order order) {
        String phone = order.getPhone();
        String message = String.format(
            "Your order #%d has been placed successfully. Order amount: ₹%.2f. It will be delivered on %s. Thank you for choosing Ice Factory!",
            order.getId(),
            order.getTotalAmount(),
            dateFormatter.format(order.getDeliveryDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())
        );
        
        notification notif = createNotification(phone, message, "ORDER_PLACED");
        sendSms(notif);
    }
    
    public void sendOrderStatusUpdateNotification(order order, String status) {
        String phone = order.getPhone();
        String message;
        
        switch (status.toLowerCase()) {
            case "processing":
                message = String.format("Your order #%d is now being processed. We'll update you when it's on the way!", order.getId());
                break;
            case "out_for_delivery":
                message = String.format("Good news! Your order #%d is out for delivery and will arrive today.", order.getId());
                break;
            case "delivered":
                message = String.format("Your order #%d has been delivered. Thank you for your business!", order.getId());
                break;
            case "cancelled":
                message = String.format("Your order #%d has been cancelled. For any questions, please contact our support team.", order.getId());
                break;
            default:
                message = String.format("Status update for your order #%d: %s", order.getId(), status);
                break;
        }
        
        notification notif = createNotification(phone, message, "STATUS_UPDATE");
        sendSms(notif);
    }
    
    private void sendSms(notification notification) {
        if (!smsEnabled) {
            notification.setStatus("DISABLED");
            notification.setResponseDetails("SMS sending is disabled in configuration");
            notificationRepository.save(notification);
            return;
        }
        
        try {
            Map<String, String> smsRequest = new HashMap<>();
            smsRequest.put("apikey", smsApiKey);
            smsRequest.put("sender", smsSender);
            smsRequest.put("to", notification.getRecipient());
            smsRequest.put("message", notification.getMessage());
            
            webClientBuilder.build()
                .post()
                .uri(smsGatewayUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(smsRequest)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    notification.setStatus("SENT");
                    notification.setResponseDetails(response);
                    notificationRepository.save(notification);
                })
                .doOnError(error -> {
                    notification.setStatus("FAILED");
                    notification.setResponseDetails("Error: " + error.getMessage());
                    notificationRepository.save(notification);
                })
                .subscribe();
        } catch (Exception e) {
            notification.setStatus("FAILED");
            notification.setResponseDetails("Exception: " + e.getMessage());
            notificationRepository.save(notification);
        }
    }
    
    public List<notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
    
    public notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }
    
    public List<notification> getNotificationsByRecipient(String recipient) {
        return notificationRepository.findByRecipient(recipient);
    }
    
    public List<notification> getNotificationsByStatus(String status) {
        return notificationRepository.findByStatus(status);
    }
    
    public List<notification> getNotificationsByType(String type) {
        return notificationRepository.findByNotificationType(type);
    }
    
    public boolean deleteNotification(Long id) {
        notification notif = notificationRepository.findById(id).orElse(null);
        if (notif != null) {
            notificationRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public void resendFailedNotifications() {
        List<notification> failedNotifications = notificationRepository.findByStatus("FAILED");
        for (notification notif : failedNotifications) {
            sendSms(notif);
        }
    }
}