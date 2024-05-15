package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.request.UsersRequest;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    List<UsersRequest> getAllUsers();
    Optional<UsersRequest> getUser(Long id);
    UsersRequest createUser(UsersRequest user);
    UsersRequest updateUser(UsersRequest user, Long id);
    List<CommentDTO> getUserComments(Long id);
    List<ProjectDTO> getFavoriteProjects(Long userId);
    List<ProjectDTO> getLikedProjects(Long userId);
    List<ProjectDTO> getUserProjects(Long userId);
    List<UserDTO> getUserFollowers(Long userId);
    List<UserDTO> getUserFollowings(Long userId);
    List<ProjectDTO> getUserProjectFollowings(Long userId);
    void deleteUser(Long id);
}