package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<ProjectDTO> getAllProjects();
    Optional<Project> getProjectById(Long id);
    Project createProject(Project project);
    Project updateProject(Project project, Long id);
    void deleteProject(Long id);
}
