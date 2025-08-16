package com.somo.pms.services;

import com.somo.pms.dto.request.ProjectRequest;
import com.somo.pms.dto.request.ProjectUpdateRequest;
import com.somo.pms.dto.response.ChatResponse;
import com.somo.pms.dto.response.ProjectResponse;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ProjectService {
    public ProjectResponse createProject(ProjectRequest request) throws BadRequestException;
    List<ProjectResponse> getAllProjectsByTeam(String userId, String category,String tag )throws BadRequestException;
    ProjectResponse getProjectById(String id)throws BadRequestException;
    void deleteProject(String projectId,String userId) throws BadRequestException;
    ProjectResponse updateProject(String projectId, ProjectUpdateRequest request) throws BadRequestException;
    void addUserToProject(String projectId, String userId) throws BadRequestException;
    void removeUserFromProject(String projectId, String userId) throws BadRequestException;
    ChatResponse getChatByProjectId(String projectId) throws BadRequestException;
    List<ProjectResponse> searchProjectsByKeyword(String keyword,String userId) throws BadRequestException;
}
