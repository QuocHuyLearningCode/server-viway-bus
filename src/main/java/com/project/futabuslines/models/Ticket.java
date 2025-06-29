package com.project.futabuslines.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_code")
    private String seatCode;

    @Column(name = "code_ticket", length = 100)
    private String codeTicket = "";

    @Column(name = "fullname", length = 100)
    private String fullname = "";

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "email")
    private String email = "";

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "total_money")
    private Long totalMoney;

//    @Column(name = "promotion_id")
//    private Long promotionId;

    @Column(name = "qr_code", unique = true)
    private String qrCode;

    @Column(name = "pick_up_time")
    private LocalDateTime pickUpTime;

    @Column(name = "drop_off_time")
    private LocalDateTime dropOffTime;

    @Column(name = "status")
    private String status;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @Column(name = "pick_up_point", length = 100)
    private String pickUpPoint = "";

    @Column(name = "drop_off_point", length = 100)
    private String dropOffPoint = "";

    @Column(name = "require_shuttle")
    private Boolean requireShuttle = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();

    }

}