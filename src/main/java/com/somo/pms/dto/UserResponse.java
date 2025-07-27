package com.somo.pms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.somo.pms.models.Issue;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String fullName;
    private String email;
    private int projectSize;
}
