package com.somo.pms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @Size(min = 2,message = "At last 2 Word required ")
    private String fullName;
    @Email(message = "Invalid Email Id")
    private String email;
    private int projectSize;
}
