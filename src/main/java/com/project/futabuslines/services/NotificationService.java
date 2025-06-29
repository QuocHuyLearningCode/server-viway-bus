package com.project.futabuslines.services;

import com.project.futabuslines.dtos.GetNotificationDTO;
import com.project.futabuslines.dtos.NotificationDTO;
import com.project.futabuslines.models.Notification;
import com.project.futabuslines.models.NotificationStatus;
import com.project.futabuslines.models.Ticket;
import com.project.futabuslines.repositories.NotificationRepository;
import com.project.futabuslines.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final TicketRepository ticketRepository;

    public Notification createNotification(NotificationDTO dto) {
        Notification notification = Notification.builder()
                .userId(dto.getUserId())
                .content(dto.getContent())
                .title(dto.getTitle())
                .sentTime(dto.getSentTime() != null ? dto.getSentTime() : LocalDateTime.now())
                .status(NotificationStatus.UNREAD)
                .build();
        return notificationRepository.save(notification);
    }

    public List<NotificationDTO> getUserNotifications(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private NotificationDTO toDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId()) // 👈 Gán ID
                .userId(notification.getUserId())
                .content(notification.getContent())
                .title(notification.getTitle())
                .sentTime(notification.getSentTime())
                .status(notification.getStatus())
                .build();
    }
    public void sendNotification(Long userId, String content, String title) {
        Notification notification = Notification.builder()
                .userId(userId)
                .content(content)
                .title(title)
                .sentTime(LocalDateTime.now())
                .status("UNREAD")
                .build();
        notificationRepository.save(notification);
    }

    @Scheduled(fixedRate = 60000) // mỗi 1 phút
    public void notifyUpcomingTrips() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusMinutes(30);

        List<Ticket> tickets = ticketRepository.findByPickUpTimeBetween(now, targetTime);
        for (Ticket ticket : tickets) {
            // Kiểm tra đã gửi thông báo chưa (tuỳ logic bạn)
            if (!notificationRepository.existsByUserIdAndContentContaining(ticket.getUser().getId(), ticket.getCodeTicket())
) {
                String message = "Bạn chuẩn bị lên xe lúc " +
                        ticket.getPickUpTime().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
                sendNotification(ticket.getUser().getId(), message, "Sắp đến giờ lên xe");
            }
        }
    }
    public Notification updateStatus(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo với ID: " + notificationId));
        notification.setStatus(NotificationStatus.READ);
        return notificationRepository.save(notification);
    }

    @Scheduled(fixedRate = 60000)
    public void notifyTripTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Ticket> tickets = ticketRepository.findByTrip_DepartureTime(tomorrow);

        for (Ticket ticket : tickets) {
            String content = "Bạn có chuyến đi " + ticket.getTrip().getRoute().getOrigin()
                    + " - " + ticket.getTrip().getRoute().getDestination()
                    + " khởi hành vào lúc " + ticket.getTrip().getTimeStart()
                    + " ngày " + ticket.getTrip().getDepartureTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    + ". Hãy chuẩn bị nhé!";

            if (!notificationRepository.existsByUserIdAndContentContaining(ticket.getUser().getId(), ticket.getCodeTicket())) {
                sendNotification(ticket.getUser().getId(), content, "Nhắc bạn chuyến đi ngày mai");
            }
        }
    }

    @Scheduled(fixedRate = 60000) // mỗi phút
    public void notifyBeforeTrip() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusHours(4);

        List<Ticket> tickets = ticketRepository.findByPickUpTimeBetween(now, reminderTime);
        for (Ticket ticket : tickets) {
            String title = "Alo Alo, FUTA gọi bạn!!!";
            String content = "Chỉ còn 4h nữa chuyến xe "
                    + ticket.getTrip().getRoute().getOrigin() + " - " + ticket.getTrip().getRoute().getDestination()
                    + " của bạn sẽ khởi hành lúc "
                    + ticket.getTrip().getTimeStart() + ". Chuẩn bị hành lý chưa bạn ơi?";

            if (!notificationRepository.existsByUserIdAndContentContaining(ticket.getUser().getId(), ticket.getCodeTicket())) {
                sendNotification(ticket.getUser().getId(), content, title);
            }
        }
    }
}
