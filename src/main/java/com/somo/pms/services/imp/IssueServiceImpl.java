package com.somo.pms.services.imp;

import com.somo.pms.dto.request.IssueRequest;
import com.somo.pms.dto.response.IssueResponse;
import com.somo.pms.models.Issue;
import com.somo.pms.models.Project;
import com.somo.pms.models.User;
import com.somo.pms.repositories.IssueRepository;
import com.somo.pms.repositories.ProjectRepository;
import com.somo.pms.repositories.UserRepository;
import com.somo.pms.services.IssueService;
import com.somo.pms.utils.ProjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    @Override
    public IssueResponse getIssueById(String id) throws BadRequestException {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new BadRequestException("Issue not found"));
        return ProjectUtils.issueMapToIssueResponse(issue) ;
    }

    @Override
    public List<IssueResponse> getIssuesByProjectId(String projectId) throws BadRequestException {
        Project project = projectRepository.findById(projectId).orElseThrow(()->new BadRequestException("Project not found"));
        return issueRepository
                .findIssueByProject(project)
                .stream()
                .map(
                        (issue)->
                                IssueResponse
                                        .builder()
                                        .id(issue.getId())
                                        .title(issue.getTitle())
                                        .description(issue.getDescription())
                                        .tags(issue.getTags())
                                        .projectId(issue.getProject().getId())
                                        .dueDate(issue.getDueDate())
                                        .priority(issue.getPriority())
                                        .assignee(
                                                ProjectUtils
                                                        .userMapToUserResponse(issue.getAssignee())
                                        )
                                        .status(issue.getStatus())
                                        .comments(
                                                issue
                                                        .getComments()
                                                        .stream()
                                                        .map(ProjectUtils::commentMapToCommentResponse)
                                                        .toList()
                                        )
                                        .build()
                )
                .toList();
    }

    @Override
    public IssueResponse createIssue(IssueRequest request,String userID) throws BadRequestException {
        Project project=projectRepository.findById(request.getProjectId()).orElseThrow(() -> new BadRequestException("Project not found"));
        User user=userRepository.findById(userID).orElseThrow(() -> new BadRequestException("User not found"));
        return ProjectUtils.issueMapToIssueResponse(
                issueRepository.save(
                        Issue
                            .builder()
                                .title(request.getTitle())
                                .description(request.getDescription())
                                .dueDate(request.getDueDate())
                                .status(request.getStatus())
                                .priority(request.getPriority())
                                .tags(request.getTags())
                                .assignee(user)
                                .project(project)
                            .build()
                )
        );
    }

    @Override
    public String deleteIssue(String id) throws BadRequestException {
        issueRepository
                .delete(
                        issueRepository
                                .findById(id)
                                .orElseThrow(() -> new BadRequestException("Issue Not Found")
                                )
                );
        return "Successfully deleted issue";
    }

    @Override
    public IssueResponse addUserToIssue(String issueId, String userId) throws BadRequestException {
        Issue issue = issueRepository
                .findById(issueId)
                .orElseThrow(() -> new BadRequestException("Issue not found"));
        User user=userRepository
                .findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
        issue.setAssignee(user);
        return ProjectUtils.issueMapToIssueResponse(issueRepository.save(issue));
    }

    @Override
    public IssueResponse updateStatus(String issueId, String status) throws BadRequestException {
        Issue issue = issueRepository
                .findById(issueId)
                .orElseThrow(() -> new BadRequestException("Issue not found"));
        issue.setStatus(status);
        return ProjectUtils.issueMapToIssueResponse(issueRepository.save(issue));
    }
}
