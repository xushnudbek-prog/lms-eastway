package uz.eastwaysolutions.lms.eastwaylms.service.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
@RequiredArgsConstructor
public class NotificationSocketService {

    private final SocketIOServer server;

    @PostConstruct
    public void startServer() {
        server.start();
        System.out.println("Socket.IO server started on port 9092");
    }

    @PreDestroy
    public void stopServer() {
        server.stop();
        System.out.println("Socket.IO server stopped");
    }

    @OnConnect
    public void onConnect(com.corundumstudio.socketio.SocketIOClient client) {
        String userId = client.getHandshakeData().getSingleUrlParam("userId");
        System.out.println("Client connected: " + userId);
        client.set("userId", userId);
    }

    @OnDisconnect
    public void onDisconnect(com.corundumstudio.socketio.SocketIOClient client) {
        String userId = client.get("userId");
        System.out.println("Client disconnected: " + userId);
    }

    @OnEvent("sendNotification")
    public void onSendNotification(com.corundumstudio.socketio.SocketIOClient client, String message) {
        String userId = client.get("userId");
        System.out.println("Received notification for user " + userId + ": " + message);
        client.sendEvent("receiveNotification", message);
    }
}
