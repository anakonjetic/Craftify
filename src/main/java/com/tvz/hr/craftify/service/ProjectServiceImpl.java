package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private ProjectRepository projectRepository;

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
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
}
