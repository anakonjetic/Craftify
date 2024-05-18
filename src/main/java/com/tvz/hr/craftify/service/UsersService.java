package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.service.dto.UsersGetDTO;
import com.tvz.hr.craftify.service.dto.CommentDTO;
import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.UserDTO;
import com.tvz.hr.craftify.service.dto.UsersPutPostDTO;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    List<UsersGetDTO> getAllUsers();
    Optional<UsersGetDTO> getUser(Long id);
    UsersGetDTO createUser(UsersPutPostDTO user);
    UsersGetDTO updateUser(UsersPutPostDTO user, Long id);
    Optional<List<CommentDTO>> getUserComments(Long id);
    Optional<List<ProjectDTO>> getFavoriteProjects(Long userId);
    Optional<List<ProjectDTO>> getLikedProjects(Long userId);
    Optional<List<ProjectDTO>> getUserProjects(Long userId);
    Optional<List<UserDTO>> getUserFollowers(Long userId);
    Optional<List<UserDTO>> getUserFollowings(Long userId);
    Optional<List<ProjectDTO>> getUserProjectFollowings(Long userId);
    void deleteUser(Long id);
    void addToFavorites(Long userId, Long projectId);
    void removeFromFavorites(Long userId, Long projectId);
    void userLikeAction(Long userId, Long projectId);
    void userDislikeAction(Long userId, Long projectId);
}
