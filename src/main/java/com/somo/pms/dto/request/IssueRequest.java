package com.somo.pms.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.somo.pms.models.Comment;
import com.somo.pms.models.Project;
import com.somo.pms.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class IssueRequest {
    private String title;
    private String description;
    private String status;
    private String projectId;
    private String priority;
    private LocalDate dueDate;
    private List<String> tags=new ArrayList<String>();
    private String assignee;
}
