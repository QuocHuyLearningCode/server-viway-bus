package com.project.futabuslines.response;


import com.project.futabuslines.dtos.UserLoginDTO;
import com.project.futabuslines.models.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String token;
    private Long userId;
    private String fullName;
    private Long roleId;

    public LoginResponseDTO(String token, Long userId, String fullName, Long roleId) {
        this.token = token;
        this.userId = userId;
        this.fullName = fullName;
        this.roleId = roleId;
    }

}