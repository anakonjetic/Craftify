package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.SubscriptionDTO;
import com.tvz.hr.craftify.service.dto.UserDTO;
import com.tvz.hr.craftify.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
@AllArgsConstructor
public class SubscriptionController {
    private SubscriptionService subscriptionService;

    // Retrieves followers of a user
    @GetMapping("/followers/user/{id}")
    public List<UserDTO> getFollowers(@PathVariable long id) { return subscriptionService.getUserFollowers(id); }

    // Retrieves users followed by a user
    @GetMapping("/followed/users/{id}")
    public List<UserDTO> getFollowedUsers(@PathVariable long id) { return subscriptionService.getUserFollowings(id); }

    // Retrieves projects followed by a user
    @GetMapping("/followed/projects/{id}")
    public List<ProjectDTO> getFollowedProjects(@PathVariable long id) { return subscriptionService.getUserProjectFollowings(id); }

    // Retrieves followers of a project
    @GetMapping("/followers/project/{id}")
    public List<UserDTO> getProjectFollowers(@PathVariable long id) { return subscriptionService.getProjectFollowers(id); }


    @PostMapping("/user")
    public ResponseEntity<Void> followUser(@RequestBody SubscriptionDTO sub) {
        subscriptionService.followUser(sub);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/user")
    public ResponseEntity<Void> unfollowUser(@RequestBody SubscriptionDTO sub) {
        subscriptionService.unfollowUser(sub);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/project")
    public ResponseEntity<Void> followProject(@RequestBody SubscriptionDTO sub) {
        subscriptionService.followProject(sub);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/project")
    public ResponseEntity<Void> unfollowProject(@RequestBody SubscriptionDTO sub) {
        subscriptionService.unfollowProject(sub);
        return ResponseEntity.noContent().build();
    }

}
