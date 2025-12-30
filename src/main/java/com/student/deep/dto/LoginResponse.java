package com.student.deep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String userId;
    private String userName;
    private String email;
    private String fullName;
    private String role;
    private String message;
    private boolean success;
}
