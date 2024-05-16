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
    public List<CommentDTO> getUserComments(@PathVariable long id) {
        return usersService.getUserComments(id);
    }
    @GetMapping("/favorite/{id}")
    public ResponseEntity<List<ProjectDTO>> getFavoriteProjects(@PathVariable long id) {
        List<ProjectDTO> favoriteProjects = usersService.getFavoriteProjects(id);
        if (favoriteProjects.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(favoriteProjects);
        }
    }
    @GetMapping("/liked/{id}")
    public List<ProjectDTO> getLikedProjects(@PathVariable long id) {
        return usersService.getLikedProjects(id);
    }
    @GetMapping("/projects/{id}")
    public List<ProjectDTO> getUserProjects(@PathVariable long id) {
        return usersService.getUserProjects(id);
    }
    @GetMapping("/followers/{id}")
    public List<UserDTO> getUserFollowers(@PathVariable long id) {
        return usersService.getUserFollowers(id);
    }
    @GetMapping("/following/users/{id}")
    public List<UserDTO> getUserFollowings(@PathVariable long id) {
        return usersService.getUserFollowings(id);
    }
    @GetMapping("/following/projects/{id}")
    public List<ProjectDTO> getProjectsFollowings(@PathVariable long id) {
        return usersService.getUserProjectFollowings(id);
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
