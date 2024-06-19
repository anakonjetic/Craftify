package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.TutorialRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import com.tvz.hr.craftify.utilities.exceptions.ApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class LoggedUserContentServiceTest {
    @Mock
    private UserAuthorizationService userAuthorizationService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TutorialRepository tutorialRepository;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private LoggedUserContentService loggedUserContentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllProjects_UserLoggedIn() {
        List<Category> userPreferences = List.of(
                new Category(2L, "Category 2"),
                new Category(3L, "Category 3"),
                new Category(4L, "Category 4")
        );

        Users user = new Users();
        user.setId(1L);
        user.setUserPreferences(userPreferences);
        when(userAuthorizationService.getLoggedInUser()).thenReturn(user);

        List<Project> mockProjects = new ArrayList<>();
        when(projectRepository.getAllProjectsByCategoryIdAndOrderByUserLikes(anyLong())).thenReturn(mockProjects);
        when(projectRepository.getAllProjectsAndOrderByUserLikes()).thenReturn(mockProjects);

        List<ProjectDTO> actualProjects = loggedUserContentService.getAllProjects();

        assertEquals(mockProjects.size(), actualProjects.size());
        verify(userAuthorizationService, times(1)).getLoggedInUser();
        verify(projectRepository, times(1)).getAllProjectsAndOrderByUserLikes();
    }

    @Test
    public void testGetAllProjects_UserNotLoggedIn() {
        when(userAuthorizationService.getLoggedInUser()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> loggedUserContentService.getAllProjects());

        verify(userAuthorizationService, times(1)).getLoggedInUser();
        verify(projectRepository, never()).getAllProjectsByCategoryIdAndOrderByUserLikes(anyLong());
        verify(projectRepository, never()).getAllProjectsAndOrderByUserLikes();
    }

    @Test
    public void testGetAllProjects_DataAccessException() {
        when(projectRepository.getAllProjectsByCategoryIdAndOrderByUserLikes(anyLong())).thenReturn(Collections.emptyList());
        when(projectRepository.getAllProjectsAndOrderByUserLikes()).thenReturn(Collections.emptyList());

        assertThrows(ApplicationException.class, () -> {
            loggedUserContentService.getAllProjects();
        });
    }

    @Test
    public void testGetAllProjects_UnexpectedException() {
        when(userAuthorizationService.getLoggedInUser()).thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(ApplicationException.class, () -> loggedUserContentService.getAllProjects());

        verify(userAuthorizationService, times(1)).getLoggedInUser();
        verify(projectRepository, never()).getAllProjectsByCategoryIdAndOrderByUserLikes(anyLong());
        verify(projectRepository, never()).getAllProjectsAndOrderByUserLikes();
    }

    @Test
    public void testGetAllTutorials_UserLoggedIn() {
        List<Category> userPreferences = List.of(
                new Category(2L, "Category 2"),
                new Category(3L, "Category 3"),
                new Category(4L, "Category 4")
        );

        Users user = new Users();
        user.setId(1L);
        user.setUserPreferences(userPreferences);
        when(userAuthorizationService.getLoggedInUser()).thenReturn(user);

        List<Tutorial> mockTutorials = new ArrayList<>();
        when(tutorialRepository.findByCategory_Id(anyLong())).thenReturn(mockTutorials);
        when(tutorialRepository.findAll()).thenReturn(mockTutorials);

        List<TutorialDTO> actualTutorials = loggedUserContentService.getAllTutorials();

        assertEquals(mockTutorials.size(), actualTutorials.size());
        verify(userAuthorizationService, times(1)).getLoggedInUser();
        verify(tutorialRepository, times(3)).findByCategory_Id(anyLong());
        verify(tutorialRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllTutorials_UserNotLoggedIn() {
        when(userAuthorizationService.getLoggedInUser()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> loggedUserContentService.getAllTutorials());

        verify(userAuthorizationService, times(1)).getLoggedInUser();
        verify(tutorialRepository, never()).findByCategory_Id(anyLong());
        verify(tutorialRepository, never()).findAll();
    }

    @Test
    public void testGetAllTutorials_DataAccessException() {
        when(tutorialRepository.findByCategory_Id(anyLong())).thenReturn(Collections.emptyList());
        when(tutorialRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ApplicationException.class, () -> {
            loggedUserContentService.getAllTutorials();
        });
    }

    @Test
    public void testGetAllTutorials_UnexpectedException() {
        when(userAuthorizationService.getLoggedInUser()).thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(ApplicationException.class, () -> loggedUserContentService.getAllTutorials());

        verify(userAuthorizationService, times(1)).getLoggedInUser();
        verify(tutorialRepository, never()).findByCategory_Id(anyLong());
        verify(tutorialRepository, never()).findAll();
    }
}
