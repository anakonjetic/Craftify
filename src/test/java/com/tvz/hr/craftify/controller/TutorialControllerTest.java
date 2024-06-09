package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.config.SecurityConfiguration;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.JwtService;
import com.tvz.hr.craftify.service.TutorialService;
import com.tvz.hr.craftify.service.UserDetailsServiceImpl;
import com.tvz.hr.craftify.service.UsersService;
import com.tvz.hr.craftify.service.dto.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TutorialController.class)
@Import(SecurityConfiguration.class)
public class TutorialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersService userService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private TutorialService tutorialService;

    @Test
    public void getAllTutorials_ReturnsTutorialList() throws Exception {
        // Arrange
        TutorialDTO tutorial1 = new TutorialDTO(1L, "Title1", "Description1", null, null, null, null);
        TutorialDTO tutorial2 = new TutorialDTO(2L, "Title2", "Description2", null, null, null, null);
        when(tutorialService.getAllTutorials()).thenReturn(Arrays.asList(tutorial1, tutorial2));

        // Act & Assert
        mockMvc.perform(get("/tutorial/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Title1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Title2"));
    }

    @Test
    public void getTutorialById_TutorialExists_ReturnsTutorial() throws Exception {
        // Arrange
        TutorialDTO tutorial = new TutorialDTO(1L, "Title1", "Description1", null, null, null, null);
        when(tutorialService.getTutorialById(1L)).thenReturn(Optional.of(tutorial));

        // Act & Assert
        mockMvc.perform(get("/tutorial/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Title1"));
    }

    @Test
    public void getTutorialById_TutorialDoesNotExist_ReturnsNotFound() throws Exception {
        // Arrange
        when(tutorialService.getTutorialById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/tutorial/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void createTutorial_ValidInput_ReturnsCreatedTutorial() throws Exception {
        // Arrange
        TutorialPostDTO tutorialPostDTO = new TutorialPostDTO(null, "Title1", "Description1", 1L, 1L, 1L, null);
        TutorialDTO tutorialDTO = new TutorialDTO(1L, "Title1", "Description1", null, null, null, null);
        when(tutorialService.createTutorial(any(TutorialPostDTO.class))).thenReturn(tutorialDTO);

        // Act & Assert
        mockMvc.perform(post("/tutorial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Title1\",\"content\":\"Description1\",\"userId\":1,\"categoryId\":1,\"complexityId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Title1"));
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void updateTutorial_ValidId_ReturnsUpdatedTutorial() throws Exception {
        // Arrange
        TutorialPutDTO tutorialPutDTO = new TutorialPutDTO(1L, "UpdatedTitle", "UpdatedDescription", 1L, 1L);
        TutorialDTO tutorialDTO = new TutorialDTO(1L, "UpdatedTitle", "UpdatedDescription", null, null, null, null);
        when(tutorialService.updateTutorial(any(TutorialPutDTO.class), anyLong())).thenReturn(tutorialDTO);

        // Act & Assert
        mockMvc.perform(put("/tutorial/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"title\":\"UpdatedTitle\",\"content\":\"UpdatedDescription\",\"categoryId\":1,\"complexityId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("UpdatedTitle"));
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void updateTutorial_InvalidId_ReturnsNotFound() throws Exception {
        // Arrange
        TutorialPutDTO tutorialPutDTO = new TutorialPutDTO(1L, "UpdatedTitle", "UpdatedDescription", 1L, 1L);
        when(tutorialService.updateTutorial(any(TutorialPutDTO.class), anyLong())).thenThrow(new RuntimeException("Tutorial not found"));

        // Act & Assert
        mockMvc.perform(put("/tutorial/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"title\":\"UpdatedTitle\",\"content\":\"UpdatedDescription\",\"categoryId\":1,\"complexityId\":1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void deleteTutorial_ValidId_ReturnsNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/tutorial/1"))
                .andExpect(status().isNoContent());

        // Verify
        verify(tutorialService, times(1)).deleteTutorialById(1L);
    }
}
