package uz.eastwaysolutions.lms.eastwaylms.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.eastwaysolutions.lms.eastwaylms.entity.Notification;
import uz.eastwaysolutions.lms.eastwaylms.repository.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SocketIOServer server;
    private final NotificationRepository notificationRepository;

    public void sendNotification(Long userId, String message) {
        Notification notification = new Notification(userId, message);
        notificationRepository.save(notification);

        for (SocketIOClient client : server.getAllClients()) {
            String connectedUserId = client.get("userId");
            if (connectedUserId != null && connectedUserId.equals(String.valueOf(userId))) {
                client.sendEvent("receiveNotification", message);
                System.out.println("Notification sent to user " + userId);
                return;
            }
        }

        System.out.println("User " + userId + " is not connected");
    }

    public List<Notification> getUserNotifications(Long userId) {

        return notificationRepository.findByUserId(userId);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
