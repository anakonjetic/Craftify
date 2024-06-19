package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.TutorialDTO;

import java.util.List;

public interface ContentService {
    List<ProjectDTO> getAllProjects();
    List<TutorialDTO> getAllTutorials();
}
