package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.ProjectPostDTO;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<ProjectDTO> getAllProjects();
    Optional<ProjectDTO> getProjectById(Long id);
    Project createProject(ProjectPostDTO projectPostDTO);
    Project updateProject(Project project, Long id);
    void deleteProject(Long id);
}
