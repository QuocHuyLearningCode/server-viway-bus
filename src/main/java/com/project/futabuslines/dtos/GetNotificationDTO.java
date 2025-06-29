package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetNotificationDTO {
    @JsonProperty("user_id")
    private Long userId;

    private String content;

    private String channel;

    @JsonProperty("sent_time")
    private LocalDateTime sentTime;

    private String status;

}
