package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.service.dto.ProjectDTO;

import java.util.List;
import java.util.Optional;

public interface LikesAndFavoritesService {
    void addToFavorites(Long userId, Long projectId);
    void removeFromFavorites(Long userId, Long projectId);
    void userLikeAction(Long userId, Long projectId);
    void userDislikeAction(Long userId, Long projectId);
    Optional<List<ProjectDTO>> getFavoriteProjects(Long userId);
    Optional<List<ProjectDTO>> getLikedProjects(Long userId);
}
