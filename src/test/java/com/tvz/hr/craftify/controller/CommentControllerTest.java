package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.config.SecurityConfiguration;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.CommentService;
import com.tvz.hr.craftify.service.JwtService;
import com.tvz.hr.craftify.service.UserDetailsServiceImpl;
import com.tvz.hr.craftify.service.UsersService;
import com.tvz.hr.craftify.service.dto.CommentDTO;
import com.tvz.hr.craftify.service.dto.CommentPostPutDTO;
import com.tvz.hr.craftify.service.dto.ProjectGetDTO;
import com.tvz.hr.craftify.service.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@Import(SecurityConfiguration.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UsersService userService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetAllComments() throws Exception {
        UserDTO user = new UserDTO();
        user.setId(1L);
        ProjectGetDTO project = new ProjectGetDTO();
        project.setId(1L);
        CommentDTO comment1 = new CommentDTO(1L, "Comment 1", user, project, LocalDateTime.now());
        CommentDTO comment2 = new CommentDTO(2L, "Comment 2", user, project, LocalDateTime.now());

        when(commentService.getAllComments()).thenReturn(Arrays.asList(comment1, comment2));

        mockMvc.perform(get("/comments/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].comment").value("Comment 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].comment").value("Comment 2"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetComment() throws Exception {
        UserDTO user = new UserDTO();
        user.setId(1L);
        ProjectGetDTO project = new ProjectGetDTO();
        project.setId(1L);
        CommentDTO comment = new CommentDTO(1L, "Comment 1", user, project, LocalDateTime.now());
        when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));

        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.comment").value("Comment 1"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetComment_NotFound() throws Exception {
        when(commentService.getCommentById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetCommentsByProject() throws Exception {
        UserDTO user = new UserDTO();
        user.setId(1L);
        ProjectGetDTO project = new ProjectGetDTO();
        project.setId(1L);
        CommentDTO comment = new CommentDTO(1L, "Comment 1", user, project, LocalDateTime.now());
        when(commentService.getCommentsByProject(1L)).thenReturn(Arrays.asList(comment));

        mockMvc.perform(get("/comments/byProject/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].comment").value("Comment 1"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCreateComment() throws Exception {
        UserDTO user = new UserDTO();
        user.setId(1L);
        ProjectGetDTO project = new ProjectGetDTO();
        project.setId(1L);
        CommentPostPutDTO commentPostDTO = new CommentPostPutDTO(2L, "New Comment", 1L, 1L, 1L);
        CommentDTO commentDTO = new CommentDTO(2L, "New Comment", user, project, LocalDateTime.now());

        when(commentService.createComment(any(CommentPostPutDTO.class))).thenReturn(commentDTO);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"New Comment\", \"userId\":1, \"projectId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.comment").value("New Comment"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateComment() throws Exception {
        UserDTO user = new UserDTO();
        user.setId(1L);
        ProjectGetDTO project = new ProjectGetDTO();
        project.setId(1L);
        CommentPostPutDTO commentPutDTO = new CommentPostPutDTO(2L, "New Comment", 1L, 1L, 1L);
        CommentDTO commentDTO = new CommentDTO(2L, "Updated Comment", user, project, LocalDateTime.now());

        when(commentService.updateComment(any(CommentPostPutDTO.class), anyLong())).thenReturn(commentDTO);

        mockMvc.perform(put("/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"Updated Comment\", \"userId\":1, \"projectId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.comment").value("Updated Comment"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateComment_NotFound() throws Exception {
        when(commentService.updateComment(any(CommentPostPutDTO.class), anyLong())).thenThrow(new RuntimeException("Comment not found"));

        mockMvc.perform(put("/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"Updated Comment\", \"userId\":1, \"projectId\":1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/comments/1"))
                .andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteComment(1L);
    }
}
