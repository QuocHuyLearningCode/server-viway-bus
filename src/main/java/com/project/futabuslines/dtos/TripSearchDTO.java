package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TripSearchDTO {
    @NotBlank(message = "Điểm đi không được để trống") // <===== Điểm đi
    private String origin;

    @NotBlank(message = "Điểm đến không được để trống") // <===== Điểm đến
    private String destination;

    @JsonProperty("departure_date") // <===== Ngày đi
    @NotNull(message = "Ngày đi không được để trống")
    @FutureOrPresent(message = "Ngày đi phải là hôm nay hoặc tương lai")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate departureDate;

    @JsonProperty("return_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Future(message = "Ngày về phải là sau ngày hôm nay")
    private LocalDate returnDate; // <- mới thêm cho chuyến về

    @JsonProperty("is_round_trip")
    private boolean isRoundTrip = false; // <- cờ xác định có phải khứ hồi không

    @Min(value = 1, message = "Số lượng hành khách phải lớn hơn 0") // <===== Số vé
    private Integer passengers = 1;

}
