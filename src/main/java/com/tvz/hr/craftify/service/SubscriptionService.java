package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Project;

import java.util.List;

public interface SubscriptionService {
    List<UserDTO> getUserFollowers(Long userId);
    List<UserDTO> getUserFollowings(Long userId);
    List<ProjectDTO> getUserProjectFollowings(Long userId);
    List<UserDTO> getProjectFollowers(Long projectId);
    void followProject(SubscriptionDTO sub);
    void unfollowProject(SubscriptionDTO sub);
    void followUser(SubscriptionDTO sub);
    void unfollowUser(SubscriptionDTO sub);
}
