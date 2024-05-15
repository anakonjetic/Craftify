package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.request.UsersRequest;
import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.ProjectPostDTO;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private ProjectRepository projectRepository;
    private CategoryService categoryService;
    private UsersService usersService;

    //TODO usera kreatora namapirati
    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList());
    }

    //TODO usera kreatora namapirati
    @Override
    public Optional<ProjectDTO> getProjectById(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        return optionalProject.map(MapToDTOHelper::mapToProjectDTO);
    }

    @Override
    public Project createProject(ProjectPostDTO postProject) {
        Project newProject = new Project();
        newProject.setTitle(postProject.getTitle());
        newProject.setDescription(postProject.getDescription());
        newProject.setContent(postProject.getContent());
        //TODO Fetch and set user nedostaje
        newProject.setUser(new Users(1L, "john_doe"));
        Category category = categoryService.getCategoryById(postProject.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        newProject.setCategory(category);
       /* List<UsersRequest> userRequestLikes = usersService.getAllUsers().stream().filter(u -> postProject.getUserLikesIdList().contains(u.getId())).collect(Collectors.toList());
        List<Users>*/
        //TODO Fetch and set complexity nedostaje
        //TODO Fetch and set media nedostaje
        //TODO Fetch and set comment nedostaje
        //TODO Fetch and set userLikes nedostaje
        //TODO Fetch and set favoriteProjects nedostaje
        //TODO Fetch and set userFollowers nedostaje

        return projectRepository.save(newProject);
    }

    @Override
    public Project updateProject(Project project, Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project projectToUpdate = optionalProject.get();
            projectToUpdate.setTitle(project.getTitle());
            projectToUpdate.setDescription(project.getDescription());
            projectToUpdate.setContent(project.getContent());
            projectToUpdate.setCategory(project.getCategory());
            projectToUpdate.setComplexity(project.getComplexity());
            return projectRepository.save(projectToUpdate);
        } else {
            throw new RuntimeException("Project with ID: " + id + " not found");
        }
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }



}
