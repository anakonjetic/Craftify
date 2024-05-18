package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.service.dto.*;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<ProjectDTO> getAllProjects();
    Optional<ProjectDTO> getProjectById(Long id);
    Optional<List<UserDTO>> getUsersWhoLikedProject(Long id);
    Project createProject(ProjectPostDTO projectPostDTO);
    Project updateProject(ProjectPutDTO project, Long id);
    void deleteProject(Long id);
    Optional<List<ProjectGetDTO>> getFilteredProjects(FilterProjectDTO filterProjectDTO);
}
