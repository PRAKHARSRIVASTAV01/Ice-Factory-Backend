package com.application.Controller.Admin;

import com.application.Object.notification;
import com.application.Service.notificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("api/admin/notifications")
public class notificationController {

    @Autowired
    private notificationService notificationService;
    
    @GetMapping("")
    public ResponseEntity<List<notification>> getAllNotifications() {
        try {
            List<notification> notifications = notificationService.getAllNotifications();
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<notification> getNotificationById(@PathVariable Long id) {
        try {
            notification notification = notificationService.getNotificationById(id);
            if (notification != null) {
                return new ResponseEntity<>(notification, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/recipient/{phone}")
    public ResponseEntity<List<notification>> getNotificationsByRecipient(@PathVariable String phone) {
        try {
            List<notification> notifications = notificationService.getNotificationsByRecipient(phone);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<notification>> getNotificationsByStatus(@PathVariable String status) {
        try {
            List<notification> notifications = notificationService.getNotificationsByStatus(status);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<notification>> getNotificationsByType(@PathVariable String type) {
        try {
            List<notification> notifications = notificationService.getNotificationsByType(type);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteNotification(@PathVariable Long id) {
        try {
            boolean result = notificationService.deleteNotification(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/resend-failed")
    public ResponseEntity<Void> resendFailedNotifications() {
        try {
            notificationService.resendFailedNotifications();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}