package com.somo.pms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotEmpty(message = "Full Name Required")
    @Size(min = 2,message = "At last 2 Word required ")
    private String fullName;
    @NotEmpty(message = "Email Required")
    @Email(message = "Invalid Email Id")
    private String email;
    @NotEmpty
    @Size(min = 5,message = "Minimum 5 word required")
    private String password;
}
