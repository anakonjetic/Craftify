package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.SubscriptionDTO;
import com.tvz.hr.craftify.service.dto.UserDTO;
import com.tvz.hr.craftify.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subscription")
@AllArgsConstructor
@CrossOrigin(origins = {"http://test-craftify.vercel.app", "http://localhost:4200"})
public class SubscriptionController {
    private SubscriptionService subscriptionService;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/followers/user/{id}")
    public ResponseEntity<List<UserDTO>> getFollowers(@PathVariable long id) {
        Optional<List<UserDTO>> followersOptional = subscriptionService.getUserFollowers(id);
        return followersOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/followed/users/{id}")
    public ResponseEntity<List<UserDTO>> getFollowedUsers(@PathVariable long id) {
        Optional<List<UserDTO>> followedUsersOptional = subscriptionService.getUserFollowings(id);
        return followedUsersOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/followed/projects/{id}")
    public ResponseEntity<List<ProjectDTO>> getFollowedProjects(@PathVariable long id) {
        Optional<List<ProjectDTO>> followedProjectsOptional = subscriptionService.getUserProjectFollowings(id);
        return followedProjectsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/followers/project/{id}")
    public ResponseEntity<List<UserDTO>> getProjectFollowers(@PathVariable long id) {
        Optional<List<UserDTO>> projectFollowersOptional = subscriptionService.getProjectFollowers(id);
        return projectFollowersOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/user")
    public ResponseEntity<Void> followUser(@RequestBody SubscriptionDTO sub) {
        subscriptionService.followUser(sub);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/user")
    public ResponseEntity<Void> unfollowUser(@RequestBody SubscriptionDTO sub) {
        subscriptionService.unfollowUser(sub);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/project")
    public ResponseEntity<Void> followProject(@RequestBody SubscriptionDTO sub) {
        subscriptionService.followProject(sub);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/project")
    public ResponseEntity<Void> unfollowProject(@RequestBody SubscriptionDTO sub) {
        subscriptionService.unfollowProject(sub);
        return ResponseEntity.noContent().build();
    }

}
