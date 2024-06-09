package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.SubscriptionDTO;
import com.tvz.hr.craftify.service.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SubscriptionService {
    Optional<List<UserDTO>> getUserFollowers(Long userId);
    Optional<List<UserDTO>> getUserFollowings(Long userId);
    Optional<List<ProjectDTO>> getUserProjectFollowings(Long userId);
    Optional<List<UserDTO>> getProjectFollowers(Long projectId);
    void followProject(SubscriptionDTO sub);
    void unfollowProject(SubscriptionDTO sub);
    void followUser(SubscriptionDTO sub);
    void unfollowUser(SubscriptionDTO sub);
}
