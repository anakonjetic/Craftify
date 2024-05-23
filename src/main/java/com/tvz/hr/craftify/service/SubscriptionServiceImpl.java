package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.SubscriptionDTO;
import com.tvz.hr.craftify.service.dto.UserDTO;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import com.tvz.hr.craftify.utilities.exceptions.ApplicationException;
import com.tvz.hr.craftify.utilities.exceptions.DatabaseOperationException;
import com.tvz.hr.craftify.utilities.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{
    private UsersRepository usersRepository;
    private ProjectRepository projectRepository;

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

    @Override
    public Optional<List<UserDTO>> getProjectFollowers(Long projectId) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        return projectOptional.map(project -> project.getProjectFollowers().stream()
                .map(MapToDTOHelper::mapToUserDTO)
                .collect(Collectors.toList()));
    }
    @Override
    public void followUser(SubscriptionDTO sub){
        try {
            Users user = usersRepository.findById(sub.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + sub.getUserId()));
            Users followedUser = usersRepository.findById(sub.getFollowingId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + sub.getFollowingId()));
            List<Users> followingUsers = user.getFollowedUsers();

            if (followingUsers.contains(followedUser)) {
                throw new IllegalStateException("User is already following the specified user");
            }
            followingUsers.add(followedUser);
            user.setFollowedUsers(followingUsers);
            usersRepository.save(user);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to follow user due to database error", e);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while following user", e);
        }
    }
    @Override
    public void unfollowUser(SubscriptionDTO sub){
        try {
            Users user = usersRepository.findById(sub.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + sub.getUserId()));
            Users followedUser = usersRepository.findById(sub.getFollowingId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + sub.getFollowingId()));
            List<Users> followingUsers = user.getFollowedUsers();

            if (!followingUsers.contains(followedUser)) {
                throw new IllegalStateException("User is not following the specified user");
            }
            followingUsers.remove(followedUser);
            user.setFollowedUsers(followingUsers);
            usersRepository.save(user);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to unfollow user due to database error", e);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while unfollowing user", e);
        }
    }
    @Override
    public void followProject(SubscriptionDTO sub){
        Users user = usersRepository.findById(sub.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + sub.getUserId()));
        Project followedProject = projectRepository.findById(sub.getFollowingId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + sub.getFollowingId()));
        List<Project> followingProjects = user.getFollowingProjects();

        if (followingProjects.contains(followedProject)) {
            throw new IllegalStateException("User is already following the specified project");
        }
        followingProjects.add(followedProject);
        user.setFollowingProjects(followingProjects);
        try {
            usersRepository.save(user);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to follow project due to database error", e);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while following project", e);
        }
    }
    @Override
    public void unfollowProject(SubscriptionDTO sub){
        Users user = usersRepository.findById(sub.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + sub.getUserId()));
        Project followedProject = projectRepository.findById(sub.getFollowingId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + sub.getFollowingId()));
        List<Project> followingProjects = user.getFollowingProjects();

        if (!followingProjects.contains(followedProject)) {
            throw new IllegalStateException("User is not following the specified project");
        }
        followingProjects.remove(followedProject);
        user.setFollowingProjects(followingProjects);
        try {
            usersRepository.save(user);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to unfollow project due to database error", e);
        } catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while unfollowing project", e);
        }
    }
}
