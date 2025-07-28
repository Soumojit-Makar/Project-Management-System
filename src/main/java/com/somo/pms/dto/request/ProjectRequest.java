package com.somo.pms.dto.request;

import com.somo.pms.models.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectRequest {
    @NotBlank(message = "Project Name required")
    private String name;
    @NotBlank(message = "Project Description required")
    private String description;
    @NotBlank(message = "Project Category required")
    private String category;
    private List<String> tags= new ArrayList<>();
    @NotBlank(message = "Project OwnerId required")
    private String ownerId;
    private List<User> team=new ArrayList<>();
}
