package uz.eastwaysolutions.lms.eastwaylms.controller;

import org.springframework.web.bind.annotation.*;
import uz.eastwaysolutions.lms.eastwaylms.entity.Notification;
import uz.eastwaysolutions.lms.eastwaylms.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/user/notifications")
public class NotificationController {


    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/unread")
    public List<Notification> getUnreadNotifications(@RequestParam Long userId) {
        return notificationService.getUnreadNotifications(userId);
    }

    @PutMapping("/mark-as-read")
    public String markNotificationsAsRead(@RequestBody List<Long> notificationIds) {
        notificationService.markNotificationsAsRead(notificationIds);
        return "Notifications marked as read";
    }
}

