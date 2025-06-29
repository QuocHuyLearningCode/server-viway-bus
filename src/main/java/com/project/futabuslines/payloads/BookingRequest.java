package com.project.futabuslines.payloads;

import lombok.Data;
import java.util.List;

@Data
public class BookingRequest {
    private Long trip_id;
    private Long user_id;
    private List<String> seat_code;
    private String full_name;
    private String phone_number;
    private String email;
    private String pick_up_point;
    private String drop_off_point;
    private boolean require_shuttle;
}
