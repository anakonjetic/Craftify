package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private ProjectRepository projectRepository;
    private CategoryService categoryService;

    //TODO usera kreatora namapirati
    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //TODO usera kreatora namapirati
    @Override
    public Optional<ProjectDTO> getProjectById(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        return optionalProject.map(this::mapToDTO);
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


    //TODO usera kreatora namapirati
    private ProjectDTO mapToDTO(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getContent(),
                project.getCategory(),
                project.getComplexity(),
                project.getMediaList().stream().map(this::mapToMediaDTO).collect(Collectors.toList()),
                project.getComments().stream().map(this::mapToCommentDTO).collect(Collectors.toList()),
                project.getUserLikes().stream().map(this::mapToUserDTO).collect(Collectors.toList()),
                project.getFavoriteProjects().stream().map(this::mapToUserDTO).collect(Collectors.toList()),
                project.getProjectFollowers().stream().map(this::mapToUserDTO).collect(Collectors.toList())
        );
    }

    private UserDTO mapToUserDTO(Users user) {
        return new UserDTO(
                user.getId(),
                user.getUsername()
        );
    }

    private MediaDTO mapToMediaDTO(Media media) {
        return new MediaDTO(
                media.getId(),
                media.getMedia(),
                media.getMediaOrder()
        );
    }

    private CommentDTO mapToCommentDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getComment(),
                mapToUserDTO(comment.getUser()),
                comment.getCommentTime()
        );
    }
}
