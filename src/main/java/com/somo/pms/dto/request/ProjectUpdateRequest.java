package com.somo.pms.dto.request;

import com.somo.pms.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjectUpdateRequest {
    private String name;
    private String description;
    private String category;
    private List<String> tags= new ArrayList<>();
    private String ownerId;
    private List<User> team=new ArrayList<>();
}
