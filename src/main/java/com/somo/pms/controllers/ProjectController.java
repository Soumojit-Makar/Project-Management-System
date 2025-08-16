package com.somo.pms.controllers;
import com.somo.pms.dto.request.InvitationSendRequest;
import com.somo.pms.dto.request.ProjectRequest;
import com.somo.pms.dto.request.ProjectUpdateRequest;
import com.somo.pms.dto.response.*;
import com.somo.pms.services.InvitationService;
import com.somo.pms.services.ProjectService;
import com.somo.pms.services.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;
    private final InvitationService invitationService;
    // Post Mappings
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @RequestBody ProjectRequest projectRequest,
            @RequestHeader("Authorization") String jwt
    ) throws BadRequestException {
        userService.findUserIdByJWT(jwt);
        return new ResponseEntity<>(projectService.createProject(projectRequest), HttpStatus.CREATED);
    }


    @PostMapping("/invite")
    public ResponseEntity<MessageResponse> inviteProject(
            @RequestBody InvitationSendRequest request,
            @RequestHeader("Authorization") String jwt
    ) throws BadRequestException, MessagingException {
        userService.findUserIdByJWT(jwt);
        invitationService.sendInvite(request);
        return new ResponseEntity<>(MessageResponse
                .builder()
                .createdAt(LocalDateTime.now())
                .content("invite successfully sent to the project!")
                .build(), HttpStatus.CREATED);
    }

    // Get Mapping
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestHeader("Authorization") String jwt
    ) throws BadRequestException {
        String userId= userService.findUserIdByJWT(jwt);
        return ResponseEntity.ok(projectService.getAllProjectsByTeam(userId,category,tag));
    }
    @GetMapping("/accept_invite")
    public ResponseEntity<ProjectResponse> acceptInvitations(
            @RequestParam(required = false) String token,
            @RequestHeader("Authorization") String jwt
    ) throws BadRequestException {
        String userId= userService.findUserIdByJWT(jwt);
        InvitationResponse invitation = invitationService.acceptInvitation(token, userId);
        projectService.addUserToProject(invitation.getProjectId(), userId);

        return new ResponseEntity<>(
                projectService
                        .getProjectById(
                                invitation.getProjectId()
                        ),
                HttpStatus.ACCEPTED
                );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(
            @PathVariable String id,
            @RequestHeader("Authorization") String jwt
    ) throws BadRequestException {
        userService.findUserIdByJWT(jwt);
        return ResponseEntity.ok(projectService.getProjectById(id));
    }


    @GetMapping("/chat/{id}")
    public ResponseEntity<ChatResponse> getChatByProjectId(
            @PathVariable String id,
            @RequestHeader("Authorization") String jwt

    ) throws BadRequestException {
        userService.findUserIdByJWT(jwt);
        return ResponseEntity.ok(projectService.getChatByProjectId(id));
    }


    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponse>> searchProject(
            @RequestParam(required = true) String keyword,
            @RequestHeader("Authorization") String jwt
    ) throws BadRequestException {
        String id = userService.findUserIdByJWT(jwt);
        return ResponseEntity.ok(
                projectService
                        .searchProjectsByKeyword(keyword,id)
        );
    }

    // Patch Mapping
    @PatchMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable String projectId,
            @RequestBody ProjectUpdateRequest projectRequest,
            @RequestHeader("Authorization") String jwt
    ) throws BadRequestException {
        userService.findUserByJWT(jwt);
        return new ResponseEntity<>(
                projectService
                        .updateProject(
                                projectId,
                                projectRequest
                        ),
                HttpStatus.ACCEPTED
        );
    }

    // Delete Mapping
    @DeleteMapping("/{projectId}")
    public ResponseEntity<APIResponse> deleteProject(
            @PathVariable String projectId,
            @RequestHeader("Authorization") String jwt
    ) throws BadRequestException {
        String userId = userService.findUserIdByJWT(jwt);
        projectService.deleteProject(projectId,userId);
        return ResponseEntity.ok(
                APIResponse
                        .builder()
                        .response("Project deleted successfully")
                        .build()
        );
    }

}
