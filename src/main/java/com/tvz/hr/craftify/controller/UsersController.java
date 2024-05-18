package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.service.dto.UsersGetDTO;
import com.tvz.hr.craftify.service.dto.CommentDTO;
import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.UserDTO;
import com.tvz.hr.craftify.service.UsersService;
import com.tvz.hr.craftify.service.dto.UsersPutPostDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {
    private UsersService usersService;

    @GetMapping("/all")
    public List<UsersGetDTO> getUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersGetDTO> getUser(@PathVariable long id) {
        Optional<UsersGetDTO> userOptional = usersService.getUser(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<UsersGetDTO> createUser(@RequestBody UsersPutPostDTO user) {
        return new ResponseEntity<>(
                usersService.createUser(user),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersGetDTO> updateUser(@RequestBody UsersPutPostDTO user,
                                                  @PathVariable Long id)
    {
        try {
            UsersGetDTO updatedUser = usersService.updateUser(user, id);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<List<CommentDTO>> getUserComments(@PathVariable long id) {
        Optional<List<CommentDTO>> userCommentsOptional = usersService.getUserComments(id);
        return userCommentsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
    @GetMapping("/favorite/{id}")
    public ResponseEntity<List<ProjectDTO>> getFavoriteProjects(@PathVariable long id) {
        Optional<List<ProjectDTO>> favoriteProjectsOptional = usersService.getFavoriteProjects(id);
        return favoriteProjectsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/liked/{id}")
    public ResponseEntity<List<ProjectDTO>> getLikedProjects(@PathVariable long id) {
        Optional<List<ProjectDTO>> likedProjectsOptional = usersService.getLikedProjects(id);
        return likedProjectsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<List<ProjectDTO>> getUserProjects(@PathVariable long id) {
        Optional<List<ProjectDTO>> userProjectsOptional = usersService.getUserProjects(id);
        return userProjectsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/followers/{id}")
    public ResponseEntity<List<UserDTO>> getUserFollowers(@PathVariable long id) {
        Optional<List<UserDTO>> userFollowersOptional = usersService.getUserFollowers(id);
        return userFollowersOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/following/users/{id}")
    public ResponseEntity<List<UserDTO>> getUserFollowings(@PathVariable long id) {
        Optional<List<UserDTO>> userFollowingsOptional = usersService.getUserFollowings(id);
        return userFollowingsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/following/projects/{id}")
    public ResponseEntity<List<ProjectDTO>> getProjectsFollowings(@PathVariable long id) {
        Optional<List<ProjectDTO>> projectsFollowingsOptional = usersService.getUserProjectFollowings(id);
        return projectsFollowingsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/{userId}/addFavorite/{projectId}")
    public ResponseEntity<Void> addProjectToFavorites(@PathVariable long userId, @PathVariable long projectId) {
        usersService.addToFavorites(userId, projectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/removeFavorite/{projectId}")
    public ResponseEntity<Void> removeProjectFromFavorites(@PathVariable long userId, @PathVariable long projectId) {
        usersService.removeFromFavorites(userId, projectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/like/{projectId}")
    public ResponseEntity<Void> likeAProjectByUser(@PathVariable long userId, @PathVariable long projectId) {
        usersService.userLikeAction(userId, projectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/dislike/{projectId}")
    public ResponseEntity<Void> dislikeAProjectByUser(@PathVariable long userId, @PathVariable long projectId) {
        usersService.userDislikeAction(userId, projectId);
        return ResponseEntity.noContent().build();
    }
}
