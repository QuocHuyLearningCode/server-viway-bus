package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Notification;
import com.project.futabuslines.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    boolean existsByUserIdAndContentContaining(Long userId, String content);
    // để tránh gửi trùng

}
