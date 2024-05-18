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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{
    private UsersRepository usersRepository;
    private ProjectRepository projectRepository;
    @Override
    public List<UserDTO> getUserFollowers(Long userId){
        List<Users> users = usersRepository.findById(userId).get().getFollowers();
        return users.stream()
                .map(MapToDTOHelper::mapToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUserFollowings(Long userId){
        List<Users> users = usersRepository.findById(userId).get().getFollowedUsers();
        return users.stream()
                .map(MapToDTOHelper::mapToUserDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<ProjectDTO> getUserProjectFollowings(Long userId){
        List<Project> projects = usersRepository.findById(userId).get().getFollowingProjects();
        return projects.stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList());
    };
    @Override
    public List<UserDTO> getProjectFollowers(Long projectId) {
        List<Users> users = projectRepository.findById(projectId).get().getProjectFollowers();
        return users.stream()
                .map(MapToDTOHelper::mapToUserDTO)
                .collect(Collectors.toList());
    };
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
        Users user = usersRepository.findById(sub.getUserId()).get();
        Project followedProject = projectRepository.findById(sub.getFollowingId()).get();
        List<Project> followingProjects = usersRepository.findById(sub.getUserId()).get().getFollowingProjects();

        if(!followingProjects.contains(followedProject)){
            followingProjects.add(followedProject);
            user.setFollowingProjects(followingProjects);
            usersRepository.save(user);
        }
    }
    @Override
    public void unfollowProject(SubscriptionDTO sub){
        Users user = usersRepository.findById(sub.getUserId()).get();
        Project followedProject = projectRepository.findById(sub.getFollowingId()).get();
        List<Project> followingProjects = usersRepository.findById(sub.getUserId()).get().getFollowingProjects();

        if(followingProjects.contains(followedProject)){
            followingProjects.remove(followedProject);
            user.setFollowingProjects(followingProjects);
            usersRepository.save(user);
        }
    }




}
