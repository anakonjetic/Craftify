package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.service.dto.CommentDTO;
import com.tvz.hr.craftify.service.dto.ProjectDTO;

import java.util.List;
import java.util.Optional;

public interface UserActivityService {
    Optional<List<CommentDTO>> getUserComments(Long id);
    Optional<List<ProjectDTO>> getUserProjects(Long userId);
}
