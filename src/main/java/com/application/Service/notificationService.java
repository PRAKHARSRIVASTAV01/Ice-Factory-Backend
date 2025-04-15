package com.application.Service;

import com.application.Object.notification;
import com.application.Object.order;
import com.application.Repository.notificationRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

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
        // Ensure phone is a string, not an object
        String phone = order.getPhone();
        if (phone == null || phone instanceof Object) {
            System.err.println("Invalid phone in order: " + order.getId());
            return;
        }
        
        // Rest of your existing code
        String message = String.format(
            "Your order #ORD-%d has been placed successfully. Your Order has to be Confirmed by the Owner. It will be delivered on %s. We will contact you shortly. Thank you for choosing Ice Factory!",
            order.getId(),
            dateFormatter.format(order.getDeliveryDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())
        );
        
        notification notif = createNotification(phone, message, "ORDER_PLACED");
        sendSms(notif);
    }
    
    public void sendOrderStatusUpdateNotification(order order, String status) {
        String phone = order.getPhone();
        // Initialize message with a default value before the switch statement
        String message = String.format("Status update for your order #%d: %s", order.getId(), status);
        
        switch (status.toLowerCase()) {
            case "confirmed":
                message = String.format("Your order #ORD-%d is confirmed. Order Amount: ₹%.2f. It will be delivered on %s. Thank you for choosing Ice Factory!", 
                    order.getId(), 
                    order.getTotalAmount(), 
                    dateFormatter.format(order.getDeliveryDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())
                );
                break;
            case "rejected":
                message = String.format("Bad news! Your order #ORD-%d was rejected by the Owner.", order.getId());
                break;
            case "delivered":
                message = String.format("Your order #ORD-%d has been delivered. Thank you for your business!", order.getId());
                break;
            case "cancelled":
                message = String.format("Your order #ORD-%d has been cancelled. For any questions, please contact our support team.", order.getId());
                break;
            // The default case can be removed since we've initialized the message before the switch
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
            // Get phone and message
            String phone = notification.getRecipient();
            String message = notification.getMessage();
            
            // Make sure phone is a string, not an object
            if (phone == null || phone.contains("[object Object]")) {
                notification.setStatus("FAILED");
                notification.setResponseDetails("Invalid phone number format: " + phone);
                notificationRepository.save(notification);
                System.err.println("Invalid phone number format: " + phone);
                return;
            }
            
            // Truncate message if too long
            String truncatedMessage = truncateMessage(message, 160);
            
            System.out.println("Sending SMS to: " + phone);
            System.out.println("Message: " + truncatedMessage);
            System.out.println("API URL: " + smsGatewayUrl);
            
            // Create a MultiValueMap for form data
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("route", "v3");
            formData.add("message", truncatedMessage);
            formData.add("language", "english");
            formData.add("flash", "0");
            formData.add("numbers", phone);
            
            if (smsSender != null && !smsSender.isEmpty()) {
                formData.add("sender_id", smsSender);
            }
            
            System.out.println("Form data: " + formData);
            
            WebClient client = webClientBuilder.build();
            
            client.post()
                .uri(smsGatewayUrl)
                .header("authorization", smsApiKey)
                .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    System.out.println("SMS API Response: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean isSuccess = jsonResponse.getBoolean("return");
                        
                        if (isSuccess) {
                            notification.setStatus("SENT");
                            notification.setResponseDetails(response);
                        } else {
                            notification.setStatus("FAILED");
                            notification.setResponseDetails("Error from Fast2SMS: " + response);
                        }
                    } catch (Exception e) {
                        notification.setStatus("FAILED");
                        notification.setResponseDetails("Failed to parse response: " + e.getMessage() + ", Response: " + response);
                    }
                    notificationRepository.save(notification);
                })
                .doOnError(error -> {
                    System.err.println("Error sending SMS: " + error.getMessage());
                    
                    // Check for specific error types to provide better messages
                    if (error.getMessage().contains("400 Bad Request")) {
                        System.err.println("Request details for debugging:");
                        System.err.println("Phone: " + phone);
                        System.err.println("Message length: " + (message != null ? message.length() : "null"));
                    }
                    
                    notification.setStatus("FAILED");
                    notification.setResponseDetails("API Error: " + error.getMessage());
                    notificationRepository.save(notification);
                })
                .subscribe();
                
        } catch (Exception e) {
            System.err.println("Exception when sending SMS: " + e.getMessage());
            e.printStackTrace();
            notification.setStatus("FAILED");
            notification.setResponseDetails("Exception: " + e.getMessage());
            notificationRepository.save(notification);
        }
    }
    
    // Helper method to truncate long messages
    private String truncateMessage(String message, int maxLength) {
        if (message == null) return "";
        return message.length() <= maxLength ? message : message.substring(0, maxLength - 3) + "...";
    }
    
    // Alternative method to send OTP via Fast2SMS
    public void sendOtp(String phone, String otp) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("route", "otp");           // OTP route
            requestBody.put("variables_values", otp);  // OTP value
            requestBody.put("numbers", phone);         // Phone number
            requestBody.put("sender_id", smsSender);   // Your sender ID
            requestBody.put("template_id", "your-template-id");  // Add your template ID here
            
            String message = "Your Ice Factory verification code is: " + otp;
            notification notif = createNotification(phone, message, "OTP");
            
            System.out.println("Sending OTP to: " + phone);
            System.out.println("OTP: " + otp);
            
            WebClient client = webClientBuilder.build();
            
            client.post()
                .uri(smsGatewayUrl)
                .header("authorization", smsApiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    System.out.println("OTP API Response: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean isSuccess = jsonResponse.getBoolean("return");
                        
                        if (isSuccess) {
                            notif.setStatus("SENT");
                            notif.setResponseDetails(response);
                        } else {
                            notif.setStatus("FAILED");
                            notif.setResponseDetails("Error from Fast2SMS: " + response);
                        }
                    } catch (Exception e) {
                        notif.setStatus("FAILED");
                        notif.setResponseDetails("Failed to parse response: " + e.getMessage());
                    }
                    notificationRepository.save(notif);
                })
                .doOnError(error -> {
                    notif.setStatus("FAILED");
                    notif.setResponseDetails("API Error: " + error.getMessage());
                    notificationRepository.save(notif);
                })
                .subscribe();
                
        } catch (Exception e) {
            System.err.println("Exception when sending OTP: " + e.getMessage());
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