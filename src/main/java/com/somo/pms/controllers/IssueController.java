package com.somo.pms.controllers;

import com.somo.pms.dto.request.IssueRequest;
import com.somo.pms.dto.response.APIResponse;
import com.somo.pms.dto.response.IssueResponse;
import com.somo.pms.repositories.IssueRepository;
import com.somo.pms.repositories.ProjectRepository;
import com.somo.pms.services.IssueService;
import com.somo.pms.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;
    private final UserService userService;
    @GetMapping("/{id}")
    public ResponseEntity<IssueResponse> getIssueById(
            @PathVariable String id

    ) throws BadRequestException {
        return ResponseEntity.ok(issueService.getIssueById(id));
    }
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<IssueResponse>> getIssuesByProjectId(
            @PathVariable String projectId
    ) throws BadRequestException {
        return ResponseEntity.ok(issueService.getIssuesByProjectId(projectId));
    }

    @PostMapping
    public ResponseEntity<IssueResponse> createIssue(
            @RequestBody IssueRequest issueRequest,
            @RequestHeader("Authorization") String token
    ) throws BadRequestException {
        String userId = userService.findUserIdByJWT(token);
        if(userId == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(issueService.createIssue(issueRequest, userId));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> updateIssue(
            @PathVariable String id,
            @RequestBody IssueRequest issueRequest,
            @RequestHeader("Authorization") String token
    ) throws BadRequestException {
        String userId=userService.findUserIdByJWT(token);
        if(userId == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(
                APIResponse
                        .builder()
                        .response(issueService.deleteIssue(id))
                        .build()
        );

    }
//    @PutMapping("/{issueId}/ass")

}
