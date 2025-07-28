package com.somo.pms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueResponse {
    private String id;
    private String title;
    private String description;
    private String status;
    private String projectId;
    private String priority;
    private LocalDate dueDate;
    private List<String> tags = new ArrayList<>();
    private UserResponse assignee ;
    private List<CommentResponse> comments=new ArrayList<>();
}
