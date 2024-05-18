package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.CategoryRepository;
import com.tvz.hr.craftify.repository.CommentRepository;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.UsersGetDTO;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.exceptions.ApplicationException;
import com.tvz.hr.craftify.utilities.exceptions.DatabaseOperationException;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService{
    private final CommentRepository commentRepository;
    private UsersRepository usersRepository;
    private ProjectRepository projectRepository;
    private CategoryService categoryService;
    private CategoryRepository categoryRepository;
    //public List<UsersRequest> getAllUsers() { return usersRepository.getAllUsers(); };
    @Override
    public List<UsersGetDTO> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        return users.stream()
                .map(MapToDTOHelper::mapToUsersGetDTO)
                .collect(Collectors.toList());}
    ;
    @Override
    public Optional<UsersGetDTO> getUser(Long id) {
        Optional<Users> users = usersRepository.findById(id);
        return users.map(MapToDTOHelper::mapToUsersGetDTO);
    };

    @Override
    public UsersGetDTO createUser(UsersPutPostDTO user) {
        /*List<Category> categories = user.getUserPreferences().stream()
                .map(MapToDTOHelper::mapToCategory)
                .collect(Collectors.toList());*/
        List<Long> categoryIds = user.getUserPreferences();
        List<Category> categories = categoryIds.stream()
                .map(categoryId -> categoryRepository.findById(categoryId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        Users newUser = new Users(user.getName(), user.getUsername(),user.getEmail(),user.getPassword(), user.isAdmin(), categories);
        return MapToDTOHelper.mapToUsersGetDTO(usersRepository.save(newUser));
    };
    @Override
    public UsersGetDTO updateUser(UsersPutPostDTO user, Long id) {
        Optional<Users> optionalUser = usersRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        List<Long> categoryIds = user.getUserPreferences();
        List<Category> categories = categoryIds.stream()
                .map(categoryId -> categoryRepository.findById(categoryId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        Users existingUser = optionalUser.get();

        existingUser.setName(user.getName());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setAdmin(user.isAdmin());
        existingUser.setUserPreferences(categories);

        return MapToDTOHelper.mapToUsersGetDTO(usersRepository.save(existingUser));

    };

    @Override
    public void deleteUser(Long id) { usersRepository.deleteById(id); }


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

    @Override
    public Optional<List<CommentDTO>> getUserComments(Long id) {
        Optional<List<Comment>> comments = commentRepository.findByUserId(id);
        return comments.map(commentList -> commentList.stream()
                .map(MapToDTOHelper::mapToCommentDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<ProjectDTO>> getFavoriteProjects(Long userId) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        return userOptional.map(user -> user.getFavoriteProjects().stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<ProjectDTO>> getLikedProjects(Long userId) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        return userOptional.map(user -> user.getLikedProjects().stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<ProjectDTO>> getUserProjects(Long userId) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        return userOptional.map(user -> user.getProjects().stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<UserDTO>> getUserFollowers(Long userId) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        return userOptional.map(user -> user.getFollowers().stream()
                .map(MapToDTOHelper::mapToUserDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<UserDTO>> getUserFollowings(Long userId) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        return userOptional.map(user -> user.getFollowedUsers().stream()
                .map(MapToDTOHelper::mapToUserDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<ProjectDTO>> getUserProjectFollowings(Long userId) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        return userOptional.map(user -> user.getFollowingProjects().stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList()));
    }

}
