package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.FeedbackDTO;
import com.project.futabuslines.models.Feedback;
import com.project.futabuslines.models.User;
import com.project.futabuslines.services.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${api.prefix}/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private FeedbackService feedbackService;

    // POST: http://localhost:8080/api/v1/feedbacks
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createFeedback(
            @RequestParam(required = false) String ticketCode,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile image,
            @AuthenticationPrincipal User user
    ){
        FeedbackDTO dto = new FeedbackDTO();
        dto.setTicketCode(ticketCode);
        dto.setDescription(description);
        dto.setImage(image);

        Feedback feedback = feedbackService.createFeedback(user.getId(), dto);
        return ResponseEntity.ok(feedback);
    }

    // GET: http://localhost:8080/api/v1/feedbacks/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserFeedback(@PathVariable Long userId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByUser(userId));
    }
}
