package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    private String content;

    private String title ;

    @JsonProperty("sent_time")
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime sentTime;

    private String status;
}
