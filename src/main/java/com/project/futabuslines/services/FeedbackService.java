package com.project.futabuslines.services;

import com.project.futabuslines.dtos.FeedbackDTO;
import com.project.futabuslines.dtos.RouteSearchRequest;
import com.project.futabuslines.dtos.TripDTO;
import com.project.futabuslines.dtos.TripSearchDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.*;
import com.project.futabuslines.repositories.*;
import com.project.futabuslines.response.TripSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FeedbackService {

    private FeedbackRepository feedbackRepository;
    private UserRepository userRepository;
    private FileStorageService fileStorageService; // Nếu bạn lưu ảnh

    public Feedback createFeedback(Long userId, FeedbackDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setTicketCode(dto.getTicketCode());
        feedback.setDescription(dto.getDescription());
        feedback.setCreatedAt(LocalDateTime.now());

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            String url = fileStorageService.saveFile(dto.getImage());
            feedback.setImageUrl(url);
        }

        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbackByUser(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }
}

