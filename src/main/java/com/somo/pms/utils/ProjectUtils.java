package com.somo.pms.utils;

import com.somo.pms.dto.request.ProjectRequest;
import com.somo.pms.dto.response.*;
import com.somo.pms.dto.request.UserRequest;
import com.somo.pms.models.*;

import java.util.stream.Collectors;

public  class ProjectUtils {
    public static User userRequestMapToUser(UserRequest userRequest) {
        return User.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }
    public static UserResponse userMapToUserResponse(User user) {
        return  UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .projectSize(user.getProjectSize())
                .build();
    }
    public static ProjectResponse projectMapToProjectResponse(Project project) {
        return ProjectResponse
                .builder()
                .projectId(project.getId())
                .owner(userMapToUserResponse(project.getOwner()))
                .tags(project.getTags())
                .team(project
                        .getTeam()
                        .stream()
                        .map(ProjectUtils::userMapToUserResponse)
                        .collect(Collectors.toList())
                )
                .chat(chatMapToChatResponse(project.getChat()))
                .name(project.getName())
                .category(project.getCategory())
                .description(project.getDescription())
                .issues(project
                        .getIssues()
                        .stream()
                        .map(ProjectUtils::issueMapToIssueResponse)
                        .collect(Collectors.toList())
                )
                .build();
    }
    public static ChatResponse chatMapToChatResponse(Chat chat) {
        return ChatResponse
                .builder()
                .id(chat.getId())
                .name(chat.getName())
                .users(chat
                        .getUsers()
                        .stream()
                        .map(ProjectUtils::userMapToUserResponse)
                        .collect(Collectors.toList())
                )
                .messages(chat.getMessages().stream().map(ProjectUtils::messageMapToMessageResponse).collect(Collectors.toList()))
                .build();
    }
    public static MessageResponse messageMapToMessageResponse(Message message) {
        return MessageResponse
                .builder()
                .id(message.getId())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .sender(userMapToUserResponse(message.getSender()))
                .build();
    }
    public static IssueResponse issueMapToIssueResponse(Issue issue) {
        return IssueResponse
                .builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .assignee(userMapToUserResponse(issue.getAssignee()))
                .comments(issue
                        .getComments()
                        .stream()
                        .map(ProjectUtils::commentMapToCommentResponse)
                        .collect(Collectors.toList())
                )
                .dueDate(issue.getDueDate())
                .priority(issue.getPriority())
                .status(issue.getStatus())
                .projectId(issue.getProject().getId())
                .tags(issue.getTags())
                .build();
    }
    public static CommentResponse commentMapToCommentResponse(Comment comment) {
        return CommentResponse
                .builder()
                .id(comment.getId())
                .content(comment.getContent())
                .created(comment.getCreated())
                .user(userMapToUserResponse(comment.getUser()))
                .build();
    }
    public static Project projectRequestToProject(ProjectRequest projectRequest) {
        return Project
                .builder()
                .name(projectRequest.getName())
                .category(projectRequest.getCategory())
                .tags(projectRequest.getTags())
                .description(projectRequest.getDescription())
                .build();
    }
}
