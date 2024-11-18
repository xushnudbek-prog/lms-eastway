package uz.eastwaysolutions.lms.eastwaylms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import uz.eastwaysolutions.lms.eastwaylms.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.isRead = false")  List<Notification> findByUserId(@Param("userId") Long userId);
  List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

}