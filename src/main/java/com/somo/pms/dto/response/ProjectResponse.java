package com.somo.pms.dto.response;

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
public class ProjectResponse {
    private String projectId;
    private String name;
    private String description;
    private String category;
    private List<String> tags= new ArrayList<>();
    private List<UserResponse> team=new ArrayList<>();
    private ChatResponse chat;
    private UserResponse owner;
    private List<IssueResponse> issues=new ArrayList<>();
}
