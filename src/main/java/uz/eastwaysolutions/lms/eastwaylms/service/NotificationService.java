package uz.eastwaysolutions.lms.eastwaylms.service;

import org.springframework.stereotype.Service;
import uz.eastwaysolutions.lms.eastwaylms.entity.Notification;
import uz.eastwaysolutions.lms.eastwaylms.repository.NotificationRepository;

import java.util.List;

@Service
public class NotificationService {


    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findUnreadNotificationsByUserId(userId);
    }

    public void markNotificationsAsRead(List<Long> notificationIds) {
        notificationIds.forEach(id -> {
            Notification notification = notificationRepository.findById(id).orElse(null);
            if (notification != null) {
                notification.setRead(true);
                notificationRepository.save(notification);
            }
        });
    }
}

