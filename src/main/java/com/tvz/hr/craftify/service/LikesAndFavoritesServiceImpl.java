package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.utilities.exceptions.ApplicationException;
import com.tvz.hr.craftify.utilities.exceptions.DatabaseOperationException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LikesAndFavoritesServiceImpl implements LikesAndFavoritesService {
    private UsersRepository usersRepository;
    private ProjectRepository projectRepository;
    @Override
    public void addToFavorites(Long userId, Long projectId) {
        try {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));

            if (!user.getFavoriteProjects().contains(project)) {
                user.getFavoriteProjects().add(project);
                usersRepository.save(user);
            } else {
                throw new IllegalStateException("Project is already in favorites");
            }
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to add project to favorites due to database error", e);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while adding project to favorites", e);
        }
    }

    @Override
    public void removeFromFavorites(Long userId, Long projectId) {
        try {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
            boolean removed = user.getFavoriteProjects().removeIf(project -> project.getId().equals(projectId));

            if (removed) {
                usersRepository.save(user);
            } else {
                throw new IllegalStateException("Project not found in user's favorites");
            }
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to remove project from favorites due to database error", e);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while removing project from favorites", e);
        }
    }

    @Override
    public void userLikeAction(Long userId, Long projectId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        if (!project.getUserLikes().contains(user)) {
            user.getLikedProjects().add(project);
            usersRepository.save(user);
        }
    }

    @Override
    public void userDislikeAction(Long userId, Long projectId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getLikedProjects().removeIf(pr -> pr.getId().equals(projectId));
        usersRepository.save(user);
    }
}
