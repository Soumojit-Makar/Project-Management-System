package com.somo.pms.services.imp;

import com.somo.pms.dto.request.ProjectRequest;
import com.somo.pms.dto.response.ChatResponse;
import com.somo.pms.dto.response.ProjectResponse;
import com.somo.pms.models.Chat;
import com.somo.pms.models.Project;
import com.somo.pms.models.User;
import com.somo.pms.repositories.ProjectRepository;
import com.somo.pms.repositories.UserRepository;
import com.somo.pms.services.ProjectService;
import com.somo.pms.utils.ProjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public ProjectResponse createProject(ProjectRequest request) throws BadRequestException {
        User user = userRepository.findById(request.getOwnerId()).orElseThrow(()->new BadRequestException("User not Found"));
        Project project = ProjectUtils.projectRequestToProject(request);
        project.setOwner(user);
        project.getTeam().add(user);
        Project save = projectRepository.save(project);
        Chat chat = new Chat();
        chat.setProject(save);

        return ProjectUtils.projectMapToProjectResponse(save);
        
    }

    @Override
    public List<ProjectResponse> getAllProjectsByTeam(String userId, String category, String tag) throws BadRequestException {
        return List.of();
    }

    @Override
    public ProjectResponse getProjectById(String id) throws BadRequestException {
        return null;
    }

    @Override
    public void deleteProject(String projectId, String userId) throws BadRequestException {

    }

    @Override
    public ProjectResponse updateProject(String projectId, ProjectRequest request) throws BadRequestException {
        return null;
    }

    @Override
    public void addUserToProject(String projectId, String userId, String ownerId) throws BadRequestException {


    }

    @Override
    public void removeUserFromProject(String projectId, String userId, String ownerId) throws BadRequestException {

    }

    @Override
    public ChatResponse getChatByProjectId(String projectId) throws BadRequestException {
        return null;
    }

}
