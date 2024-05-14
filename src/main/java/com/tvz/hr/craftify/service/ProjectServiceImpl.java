package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.model.Media;
import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Users;
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

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
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
