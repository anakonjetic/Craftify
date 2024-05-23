package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.service.dto.*;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    List<UsersGetDTO> getAllUsers();
    Optional<UsersGetDTO> getUser(Long id);
    UsersGetDTO authenticateUser(LoginDTO loginDTO);
    //UsersGetDTO authenticateUser(String usernameOrEmail);
    UsersGetDTO createUser(UsersPutPostDTO user);
    UsersGetDTO updateUser(UsersPutPostDTO user, Long id);
    UsersGetDTO changeUserPassword(String newPassword, Long id);
    Optional<List<CommentDTO>> getUserComments(Long id);
    Optional<List<ProjectDTO>> getFavoriteProjects(Long userId);
    Optional<List<ProjectDTO>> getLikedProjects(Long userId);
    Optional<List<ProjectDTO>> getUserProjects(Long userId);
    UsersGetDTO setUserPreference(List<Long> categories, Long userId);
    UsersGetDTO changeUserInfoVisibility(boolean isPrivate, Long id);
    void deleteUser(Long id);
}
