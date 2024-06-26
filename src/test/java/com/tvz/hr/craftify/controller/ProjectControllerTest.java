package com.tvz.hr.craftify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvz.hr.craftify.service.*;
import com.tvz.hr.craftify.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @MockBean
    private ProjectService projectService;

    @MockBean
    private LoggedUserContentService loggedUserContentService;

    @MockBean
    private GuestUserContentService guestUserContentService;
    @MockBean
    private UserAuthorizationService userAuthorizationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProjectController(projectService)).build();
    }

    @Test
    @WithMockUser(username = "john_doe", password = "newPassword123", roles = {"USER"})
    public void getProject_ValidId_ReturnsProject() throws Exception {
        long projectId = 1L;
        ProjectDTO project = new ProjectDTO();
        project.setId(projectId);

        when(projectService.getProjectById(projectId)).thenReturn(Optional.of(project));

        mockMvc.perform(get("/project/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(projectId));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getProjects_ReturnsOk() throws Exception {
        List<ProjectDTO> projectDTOList = new ArrayList<>();

        when(projectService.getAllProjects()).thenReturn(projectDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/project/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void updateProject_ValidInput_ReturnsOk() throws Exception {
        ProjectPutDTO projectPutDTO = new ProjectPutDTO();

        mockMvc.perform(MockMvcRequestBuilders.put("/project/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(projectPutDTO)))
                .andExpect(status().isOk());

        verify(projectService, times(1)).updateProject(any(ProjectPutDTO.class), eq(1L));
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void getUsersWhoLikedProject_ValidId_ReturnsListOfUsers() throws Exception {
        long projectId = 1L;
        List<UserDTO> users = new ArrayList<>();

        when(projectService.getUsersWhoLikedProject(projectId)).thenReturn(Optional.of(users));

        mockMvc.perform(get("/project/{id}/likes", projectId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void getProjectsByUserPreferences_ValidId_ReturnsListOfProjects() throws Exception {
        long userId = 1L;
        List<ProjectGetDTO> projects = new ArrayList<>();

        when(projectService.getProjectsByUserPreference(userId)).thenReturn(Optional.of(projects));

        mockMvc.perform(get("/project/preference/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"ADMIN"})
    public void createProject_ValidProject_ReturnsCreatedProject() throws Exception {
        ProjectPostDTO projectPostDTO = new ProjectPostDTO();

        mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectPostDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void updateProject_ValidIdAndProject_ReturnsUpdatedProject() throws Exception {
        long projectId = 1L;
        ProjectPutDTO projectPutDTO = new ProjectPutDTO();

        mockMvc.perform(put("/project/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectPutDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"ADMIN"})
    public void deleteProject_ValidId_ReturnsNoContent() throws Exception {
        long projectId = 1L;

        mockMvc.perform(delete("/project/{id}", projectId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void filterProjects_ValidFilter_ReturnsListOfProjects() throws Exception {
        FilterProjectDTO filterProjectDTO = new FilterProjectDTO();
        List<ProjectGetDTO> projects = new ArrayList<>();

        when(projectService.getFilteredProjects(filterProjectDTO)).thenReturn(Optional.of(projects));

        mockMvc.perform(post("/project/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterProjectDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
}
