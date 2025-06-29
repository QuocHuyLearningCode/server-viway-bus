package com.project.futabuslines.dtos;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data // => have toString();
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
    private String ticketCode;

    private String description;

    private MultipartFile image; // Dùng khi cần upload ảnh
}
