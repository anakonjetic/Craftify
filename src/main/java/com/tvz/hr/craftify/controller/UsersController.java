package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.service.*;
import com.tvz.hr.craftify.service.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin(origins = {"http://test-craftify.vercel.app", "http://localhost:4200"})
public class UsersController {
    private UsersService usersService;
    private UserAuthorizationService userAuthorizationService;
    private UserInfoService userInfoService;
    private UserActivityService userActivityService;
    private LikesAndFavoritesService likesAndFavoritesService;
    private SubscriptionService subscriptionService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public List<UsersGetDTO> getUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UsersGetDTO> updateUser(@RequestBody UsersPutDTO user,
                                                  @PathVariable Long id)
    {
        try {
            UsersGetDTO updatedUser = usersService.updateUser(user, id);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //JSON: { "categories" : [1,2,3] }
    @PutMapping("/change-preference/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UsersGetDTO> setUserPreference(@RequestBody Map<String, List<Long>> request,
                                                  @PathVariable Long id)
    {
        List<Long> categoryIds = request.get("categories");
        try {
            UsersGetDTO updatedUser = userInfoService.setUserPreference(categoryIds, id);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //JSON: { "newPassword" : "novaLozinka123" }
    @PutMapping("/change-password/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody Map<String, String> request)
    {
        String newPassword = request.get("newPassword");
        try {
            UsersGetDTO updatedUser = userInfoService.changeUserPassword(newPassword, id);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    //JSON: { "private" : true }
    @PutMapping("/profile-visibility/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UsersGetDTO> setUserInfoVisibility(@RequestBody Map<String, Boolean> request,
                                                         @PathVariable Long id)
    {
        Boolean isPrivate = request.get("private");
        try {
            UsersGetDTO updatedUser = userInfoService.changeUserInfoVisibility(isPrivate, id);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comments/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CommentDTO>> getUserComments(@PathVariable long id) {
        Optional<List<CommentDTO>> userCommentsOptional = userActivityService.getUserComments(id);
        return userCommentsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
    @GetMapping("/favorite/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProjectDTO>> getFavoriteProjects(@PathVariable long id) {
        Optional<List<ProjectDTO>> favoriteProjectsOptional = likesAndFavoritesService.getFavoriteProjects(id);
        return favoriteProjectsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/liked/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProjectDTO>> getLikedProjects(@PathVariable long id) {
        Optional<List<ProjectDTO>> likedProjectsOptional = likesAndFavoritesService.getLikedProjects(id);
        return likedProjectsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<List<ProjectDTO>> getUserProjects(@PathVariable long id) {
        Optional<List<ProjectDTO>> userProjectsOptional = userActivityService.getUserProjects(id);
        return userProjectsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/followers/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getUserFollowers(@PathVariable long id) {
        Optional<List<UserDTO>> userFollowersOptional = subscriptionService.getUserFollowers(id);
        return userFollowersOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/following/users/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getUserFollowings(@PathVariable long id) {
        Optional<List<UserDTO>> userFollowingsOptional = subscriptionService.getUserFollowings(id);
        return userFollowingsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/following/projects/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProjectDTO>> getProjectsFollowings(@PathVariable long id) {
        Optional<List<ProjectDTO>> projectsFollowingsOptional = subscriptionService.getUserProjectFollowings(id);
        return projectsFollowingsOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/{userId}/addFavorite/{projectId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> addProjectToFavorites(@PathVariable long userId, @PathVariable long projectId) {
        likesAndFavoritesService.addToFavorites(userId, projectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/removeFavorite/{projectId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> removeProjectFromFavorites(@PathVariable long userId, @PathVariable long projectId) {
        likesAndFavoritesService.removeFromFavorites(userId, projectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/like/{projectId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> likeAProjectByUser(@PathVariable long userId, @PathVariable long projectId) {
        likesAndFavoritesService.userLikeAction(userId, projectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/dislike/{projectId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> dislikeAProjectByUser(@PathVariable long userId, @PathVariable long projectId) {
        likesAndFavoritesService.userDislikeAction(userId, projectId);
        return ResponseEntity.noContent().build();
    }
}
