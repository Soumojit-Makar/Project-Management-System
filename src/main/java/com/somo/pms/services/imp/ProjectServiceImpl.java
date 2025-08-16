package com.somo.pms.services.imp;

import com.somo.pms.dto.request.ProjectRequest;
import com.somo.pms.dto.request.ProjectUpdateRequest;
import com.somo.pms.dto.response.ChatResponse;
import com.somo.pms.dto.response.ProjectResponse;
import com.somo.pms.models.Chat;
import com.somo.pms.models.Project;
import com.somo.pms.models.User;
import com.somo.pms.repositories.ProjectRepository;
import com.somo.pms.repositories.UserRepository;
import com.somo.pms.services.ChatService;
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
    private final ChatService chatService;

    @Override
    public ProjectResponse createProject(ProjectRequest request) throws BadRequestException {
        User user = userRepository.findById(request.getOwnerId()).orElseThrow(()->new BadRequestException("User not Found"));
        Project project = ProjectUtils.projectRequestToProject(request);
        project.setOwner(user);
        project.getTeam().add(user);
        Project save = projectRepository.save(project);
        Chat chat = chatService.createChat(
                Chat.builder().project(save).build()
        );
        chat.getUsers().add(user);

        save.setChat(chat);
        save = projectRepository.save(save);
        return ProjectUtils.projectMapToProjectResponse(save);
    }

    @Override
    public List<ProjectResponse> getAllProjectsByTeam(String userId, String category, String tag) throws BadRequestException {
        User user=userRepository.findById(userId).orElseThrow(()->new BadRequestException("User not Found"));
        List<Project> projects=projectRepository.findByTeamContainingOrOwner(user,user);
        if (category!=null ) {
            projects=projects.stream().filter(p->p.getCategory().equals(category)).toList();
        }
        if (tag!=null ) {
            projects=projects.stream().filter(p->p.getTags().contains(tag)).toList();
        }

        return projects
                .stream()
                .map(ProjectUtils::projectMapToProjectResponse)
                .toList();
    }

    @Override
    public ProjectResponse getProjectById(String id) throws BadRequestException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new BadRequestException("Project Not found"));
        return ProjectUtils.projectMapToProjectResponse(project) ;
    }

    @Override
    public void deleteProject(String projectId, String userId) throws BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not Found"));
        Project project=projectRepository.findById(projectId).orElseThrow(() -> new BadRequestException("Project Not Found"));
        if (project.getOwner().equals(user)) {
            projectRepository.delete(project);
        }
        else {
            throw new BadRequestException("You can't delete this project because you are not the owner of this project");
        }
    }
    @Override
    public ProjectResponse updateProject(String projectId, ProjectUpdateRequest request) throws BadRequestException {
        Project project=projectRepository.findById(projectId).orElseThrow(() -> new BadRequestException("Project Not Found"));
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCategory(request.getCategory());
        project.setTags(request.getTags());
        Project save = projectRepository.save(project);
        return ProjectUtils.projectMapToProjectResponse(save);
    }

    @Override
    public void addUserToProject(String projectId, String userId) throws BadRequestException {
        Project project =projectRepository.findById(projectId).orElseThrow(() -> new BadRequestException("Project Not Found"));
        User user=userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User Not Found"));
        if (!project.getTeam().contains(user)) {
            project.getChat().getUsers().add(user);
            project.getTeam().add(user);

        }
        else {
            throw new BadRequestException("You can't add this project because you are already in this project");
        }
        projectRepository.save(project);

    }

    @Override
    public void removeUserFromProject(String projectId, String userId) throws BadRequestException {
        Project project =projectRepository.findById(projectId).orElseThrow(() -> new BadRequestException("Project Not Found"));
        User user=userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User Not Found"));
        if (project.getTeam().contains(user)) {
            project.getChat().getUsers().remove(user);
            project.getTeam().remove(user);

        }
        else {
            throw new BadRequestException("You can't remove this project because you are not in this project");
        }
        projectRepository.save(project);
    }

    @Override
    public ChatResponse getChatByProjectId(String projectId) throws BadRequestException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new BadRequestException("Project Not Found"));
        return ProjectUtils.chatMapToChatResponse(project.getChat());
    }

    @Override
    public List<ProjectResponse> searchProjectsByKeyword(String keyword,String  userId) throws BadRequestException {
        String partialName="%"+keyword+"%";
        return projectRepository
                .findByNameContainingAndTeamContains(
                        partialName,
                        userRepository
                                .findById(userId)
                                .orElseThrow(()->new BadRequestException("User Not Found"))
                )
                .stream()
                .map(ProjectUtils::projectMapToProjectResponse)
                .toList();
    }
}
