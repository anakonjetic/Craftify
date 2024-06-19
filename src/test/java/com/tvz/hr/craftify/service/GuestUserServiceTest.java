package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Tutorial;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.TutorialRepository;
import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.TutorialDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GuestUserServiceTest {
    @Mock
    private UserAuthorizationService userAuthorizationService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TutorialRepository tutorialRepository;

    @InjectMocks
    private GuestUserContentService guestUserContentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllProjects() {
        List<Project> projects = List.of(new Project(), new Project());
        List<Project> sortedProjects = new ArrayList<>();
        projects.get(0).setMediaList(new ArrayList<>());
        projects.get(0).setComments(new ArrayList<>());
        projects.get(0).setUserLikes(new ArrayList<>());
        projects.get(0).setFavoriteProjects(new ArrayList<>());
        projects.get(0).setProjectFollowers(new ArrayList<>());
        projects.get(1).setMediaList(new ArrayList<>());
        projects.get(1).setComments(new ArrayList<>());
        projects.get(1).setUserLikes(new ArrayList<>());
        projects.get(1).setFavoriteProjects(new ArrayList<>());
        projects.get(1).setProjectFollowers(new ArrayList<>());
        when(projectRepository.findAll()).thenReturn(projects);
        when(projectRepository.getAllProjectsAndOrderByUserLikes()).thenReturn(sortedProjects);

        List<ProjectDTO> result = guestUserContentService.getAllProjects();

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllTutorials() {
        List<Tutorial> tutorials = new ArrayList<>();
        tutorials.add(new Tutorial());

        when(tutorialRepository.findAll()).thenReturn(tutorials);

        List<TutorialDTO> tutorialDTOs = guestUserContentService.getAllTutorials();
        assertEquals(1, tutorialDTOs.size());
    }
}
