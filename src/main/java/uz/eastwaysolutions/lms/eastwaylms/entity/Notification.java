package uz.eastwaysolutions.lms.eastwaylms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    private LocalDateTime createdAt;


    public Notification(Long userId, String message) {
        this.userId = userId;
        this.message = message;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }




}

