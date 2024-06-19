package com.tvz.hr.craftify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvz.hr.craftify.service.*;
import com.tvz.hr.craftify.service.dto.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
public class UsersControllerTest {
    @MockBean
    private UsersService usersService;
    @MockBean
    private LikesAndFavoritesService likesAndFavoritesService;
    @MockBean
    private SubscriptionService subscriptionService;
    @MockBean
    private JwtService jwtService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserInfoService userInfoService;
    @MockBean
    private UserActivityService userActivityService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setup() { mockMvc = MockMvcBuilders.standaloneSetup(new UsersController(usersService, userInfoService, userActivityService, likesAndFavoritesService,subscriptionService)).build();
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void getUsers_ReturnsUsersList() throws Exception{
        UsersGetDTO user1 = new UsersGetDTO();
        user1.setId(1L);
        user1.setUsername("john_doe");

        UsersGetDTO user2 = new UsersGetDTO();
        user2.setId(2L);
        user2.setUsername("jane_smith");
        when(usersService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").value("john_doe"));
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void getUserById_ValidUserId_ReturnsUser() throws Exception{
        long userId = 1L;
        UsersGetDTO user = new UsersGetDTO();
        user.setId(userId);
        when(usersService.getUser(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/{id}",userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void getUserById_InvalidUserId_ReturnsUser() throws Exception{
        long userId = 999L;
        UsersGetDTO user = new UsersGetDTO();
        user.setId(userId);
        when(usersService.getUser(userId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/users/{id}",userId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username="john_doe", roles = {"USER"})
    public void createUser_ReturnsCreatedUser() throws Exception{
        UsersPutPostDTO usersPostDTO = new UsersPutPostDTO();
        mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(usersPostDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="john_doe", password = "newPassword123", roles = {"USER"})
    public void updateUser_ValidUserId_ReturnsUpdatedUser() throws Exception{
        long userId = 1L;
        UsersPutPostDTO usersPutDTO = new UsersPutPostDTO();

        mockMvc.perform(put("/users/{id}", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(usersPutDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="john_doe", password = "newPassword123", roles = {"USER"})
    public void updateUser_InvalidUserId_ReturnsNotFound() throws Exception{
        long userId = 999L;
        UsersPutDTO usersPutDTO = new UsersPutDTO();
        when(usersService.updateUser(usersPutDTO, userId)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usersPutDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"ADMIN"})
    public void deleteUser_ReturnsNoContent() throws Exception{
        long userId = 1L;
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void setUserPreference_ValidUserId_ReturnsUpdatedUser() throws Exception {
        long userId = 1L;
        String jsonRequest = "{\"categories\": [1, 2, 3]}";

        mockMvc.perform(put("/users/change-preference/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void setUserPreference_InvalidUserId_ReturnsNotFound() throws Exception {
        long userId = 999L;
        String jsonRequest = "{\"categories\": [1, 2, 3]}";
        String expectedErrorMessage = "User not found with ID: " + userId;
        when(userInfoService.setUserPreference(Arrays.asList(1L,2L,3L), userId)).thenThrow(new EntityNotFoundException(expectedErrorMessage));

        mockMvc.perform(put("/users/change-preference/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void changePassword_ValidPassword_ReturnsUpdatedUser() throws Exception{
        long userId = 1L;
        String jsonRequest = "{ \"newPassword\" : \"novaLozinka123\" }";
        String newPassword = "novaLozinka123";
        UsersGetDTO updatedUser = new UsersGetDTO();
        updatedUser.setId(userId);

        Mockito.when(userInfoService.changeUserPassword(newPassword, userId)).thenReturn(updatedUser);

        mockMvc.perform(put("/users/change-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void changePassword_InvalidPassword_ReturnsBadRequest() throws Exception{
        long userId = 1L;
        String jsonRequest = "{ \"newPassword\" : \"novaLozinka\" }";
        String newPassword = "novaLozinka";
        String expectedErrorMessage = "Password "+ newPassword + " is not strong enough";
        when(userInfoService.changeUserPassword(newPassword, userId)).thenThrow(new IllegalArgumentException(expectedErrorMessage));

        mockMvc.perform(put("/users/change-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void changePassword_InvalidUserId_ReturnsNotFound() throws Exception{
        long userId = 999L;
        String jsonRequest = "{ \"newPassword\" : \"novaLozinka123\" }";
        String newPassword = "novaLozinka123";
        String expectedErrorMessage = "User not found with ID: " + userId;
        when(userInfoService.changeUserPassword(newPassword, userId)).thenThrow(new EntityNotFoundException(expectedErrorMessage));

        mockMvc.perform(put("/users/change-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void setUserInfoVisibility_ValidUserId_ReturnsUpdatedUser() throws Exception{
        long userId = 1L;
        String jsonRequest = "{ \"private\" : true }";
        UsersGetDTO updatedUser = new UsersGetDTO();
        updatedUser.setId(userId);
        updatedUser.setPrivate(true);

        Mockito.when(userInfoService.changeUserInfoVisibility(true, userId)).thenReturn(updatedUser);

        mockMvc.perform(put("/users/profile-visibility/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.private").value(true));
    }
    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void setUserInfoVisibility_InvalidUserId_ReturnsNotFound() throws Exception {
        long userId = 999L;
        Map<String, Boolean> request = new HashMap<>();
        request.put("private", true);
        String expectedErrorMessage = "User not found with ID: " + userId;
        when(userInfoService.changeUserInfoVisibility(true, userId)).thenThrow(new IllegalArgumentException(expectedErrorMessage));


        mockMvc.perform(MockMvcRequestBuilders.put("/users/profile-visibility/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"private\": true}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void getUserComments_ReturnsUserComments() throws Exception {
        long userId = 1L;
        List<CommentDTO> comments = new ArrayList<>();
        CommentDTO comment = new CommentDTO();
        comment.setId(1L);
        comments.add(comment);

        Mockito.when(userActivityService.getUserComments(1L)).thenReturn(Optional.of(comments));

        mockMvc.perform(get("/users/comments/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void getFavoriteProjects_ReturnsFavoriteProjects() throws Exception {
        List<ProjectDTO> projects = new ArrayList<>();
        ProjectDTO project = new ProjectDTO();
        project.setId(1L);
        project.setTitle("Test");
        projects.add(project);

        Mockito.when(likesAndFavoritesService.getFavoriteProjects(1L)).thenReturn(Optional.of(projects));

        mockMvc.perform(get("/users/favorite/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test"));
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void getLikedProjects_ReturnsLikedProjects() throws Exception {
        List<ProjectDTO> projects = new ArrayList<>();
        ProjectDTO project = new ProjectDTO();
        project.setId(1L);
        projects.add(project);

        Mockito.when(likesAndFavoritesService.getLikedProjects(1L)).thenReturn(Optional.of(projects));

        mockMvc.perform(get("/users/liked/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void getUserProjects_ReturnsUserProjects() throws Exception {
        List<ProjectDTO> projects = new ArrayList<>();
        ProjectDTO project = new ProjectDTO();
        project.setId(1L);
        projects.add(project);

        Mockito.when(userActivityService.getUserProjects(1L)).thenReturn(Optional.of(projects));

        mockMvc.perform(get("/users/projects/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void getUserFollowers_ReturnsUserFollowers() throws Exception {
        long userId = 1L;
        List<UserDTO> followers = new ArrayList<>();
        UserDTO user = new UserDTO();
        user.setId(1L);
        followers.add(user);

        Mockito.when(subscriptionService.getUserFollowers(1L)).thenReturn(Optional.of(followers));

        mockMvc.perform(get("/users/followers/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void getUserFollowings_ReturnsUserFollowings() throws Exception {
        long userId = 1L;
        List<UserDTO> followings = new ArrayList<>();
        UserDTO user = new UserDTO();
        user.setId(1L);
        followings.add(user);

        Mockito.when(subscriptionService.getUserFollowings(1L)).thenReturn(Optional.of(followings));

        mockMvc.perform(get("/users/following/users/{id}",userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void getProjectsFollowings_ReturnsProjectsFollowings() throws Exception {
        long userId = 1L;
        List<ProjectDTO> followings = new ArrayList<>();
        ProjectDTO project = new ProjectDTO();
        project.setId(1L);
        followings.add(project);

        Mockito.when(subscriptionService.getUserProjectFollowings(1L)).thenReturn(Optional.of(followings));

        mockMvc.perform(get("/users/following/projects/{id}",userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void addProjectToFavorites_AddsFavoriteProject() throws Exception {
        long userId = 1L;
        long projectId = 1L;
        Mockito.doNothing().when(likesAndFavoritesService).addToFavorites(userId, projectId);

        mockMvc.perform(post("/users/{userId}/addFavorite/{projectId}",userId,projectId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void removeProjectFromFavorites_RemovesFavoriteProject() throws Exception {
        long userId = 1L;
        long projectId = 1L;
        Mockito.doNothing().when(likesAndFavoritesService).removeFromFavorites(userId, projectId);

        mockMvc.perform(delete("/users/{userId}/removeFavorite/{projectId}",userId,projectId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void likeAProjectByUser_LikesProject() throws Exception {
        long userId = 1L;
        long projectId = 1L;
        Mockito.doNothing().when(likesAndFavoritesService).userLikeAction(userId, projectId);

        mockMvc.perform(post("/users/{userId}/like/{projectId}",userId,projectId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="john_doe", roles={"USER"})
    public void dislikeAProjectByUser_DislikesProject() throws Exception {
        long userId = 1L;
        long projectId = 1L;
        Mockito.doNothing().when(likesAndFavoritesService).userDislikeAction(userId,projectId);

        mockMvc.perform(delete("/users/{userId}/dislike/{projectId}",userId,projectId))
                .andExpect(status().isNoContent());
    }
}
