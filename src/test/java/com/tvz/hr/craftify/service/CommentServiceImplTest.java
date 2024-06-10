package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.repository.CommentRepository;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.CommentDTO;
import com.tvz.hr.craftify.service.dto.CommentPostPutDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllComments() {
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.findAll()).thenReturn(comments);

        List<CommentDTO> result = commentService.getAllComments();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void getCommentById() {
        long id = 1L;
        Comment comment = new Comment();
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        Optional<CommentDTO> result = commentService.getCommentById(id);

        assertTrue(result.isPresent());
        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    void createComment() {
        CommentPostPutDTO commentDTO = new CommentPostPutDTO();
        Comment savedComment = new Comment();
        when(commentRepository.save(any())).thenReturn(savedComment);

        CommentDTO result = commentService.createComment(commentDTO);

        assertNotNull(result);
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    void updateComment() {
        long id = 1L;
        CommentPostPutDTO commentDTO = new CommentPostPutDTO();
        commentDTO.setId(id);
        Comment commentToUpdate = new Comment();
        when(commentRepository.findById(id)).thenReturn(Optional.of(commentToUpdate));
        when(commentRepository.save(any())).thenReturn(commentToUpdate);

        CommentDTO result = commentService.updateComment(commentDTO, id);

        assertNotNull(result);
        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    void updateComment_NotFound() {
        long id = 1L;
        CommentPostPutDTO commentDTO = new CommentPostPutDTO();
        commentDTO.setId(id);
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.updateComment(commentDTO, id));
        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, never()).save(any());
    }

    @Test
    void deleteComment() {
        long id = 1L;

        commentService.deleteComment(id);

        verify(commentRepository, times(1)).deleteById(id);
    }

    @Test
    void getCommentsByProject() {
        long projectId = 1L;
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.findByProjectId(projectId)).thenReturn(comments);

        List<CommentDTO> result = commentService.getCommentsByProject(projectId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentRepository, times(1)).findByProjectId(projectId);
    }
}