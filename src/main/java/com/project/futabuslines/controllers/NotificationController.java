package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.GetNotificationDTO;
import com.project.futabuslines.dtos.NotificationDTO;
import com.project.futabuslines.models.Notification;
import com.project.futabuslines.repositories.NotificationRepository;
import com.project.futabuslines.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    // POST: http://localhost:8080/api/v1/notifications
    @PostMapping("")
    public ResponseEntity<Notification> create(@RequestBody NotificationDTO dto) {
        return ResponseEntity.ok(notificationService.createNotification(dto));
    }

    // GET: http://localhost:8080/api/v1/notifications/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    // PUT: http://localhost:8080/api/v1/notifications/read/{id}
    @PutMapping("/read/{id}")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {
        Notification noti = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));
        noti.setStatus("READ");
        notificationRepository.save(noti);
        return ResponseEntity.ok("Đã cập nhật trạng thái thông báo");
    }

    // PATCH: http://localhost:8080/api/v1/notifications/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Notification> updateStatus(
            @PathVariable Long id) {
        Notification updated = notificationService.updateStatus(id);
        return ResponseEntity.ok(updated);
    }


}
