package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDTO {

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
//    @NotBlank(message = "Số điện thoại không được để trống!")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ!")
    private String phoneNumber;

    @Email(message = "Email không hợp lệ")
//    @NotBlank(message = "Email không được để trống!")
    private String email;
}
