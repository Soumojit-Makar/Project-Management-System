package com.somo.pms.repositories;

import com.somo.pms.models.Issue;
import com.somo.pms.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, String> {
    List<Issue> findIssueByProject(Project project);
}
