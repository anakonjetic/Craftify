package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import com.tvz.hr.craftify.utilities.exceptions.ApplicationException;
import com.tvz.hr.craftify.utilities.exceptions.DatabaseOperationException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LikesAndFavoritesServiceImplTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private LikesAndFavoritesServiceImpl likesAndFavoritesService;

    @Mock
    private JwtService jwtService;
    @Mock
    private UserAuthorizationService userAuthorizationService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UsersService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addToFavorites_UserNotFound() {
        // Given
        when(usersRepository.findById(any())).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ApplicationException.class, () -> likesAndFavoritesService.addToFavorites(1L, 1L));
    }


    @Test
    void removeFromFavorites_Success() {
        Users user = new Users();
        user.setId(1L);
        Project project = new Project();
        project.setId(1L);
        user.setFavoriteProjects(new ArrayList<>());
        user.getFavoriteProjects().add(project);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> likesAndFavoritesService.removeFromFavorites(1L, 1L));

        assertFalse(user.getFavoriteProjects().contains(project));
        verify(usersRepository, times(1)).save(user);
    }



    @Test
    void userDislikeAction_Success() {
        Users user = new Users();
        user.setId(1L);
        Project project = new Project();
        project.setId(1L);
        user.setLikedProjects(new ArrayList<>());
        user.getLikedProjects().add(project);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> likesAndFavoritesService.userDislikeAction(1L, 1L));

        assertFalse(user.getLikedProjects().contains(project));
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    void getFavoriteProjects_Success() {
        Users user = new Users();
        user.setId(1L);
        Project project1 = new Project();
        project1.setId(1L);
        project1.setMediaList(new ArrayList<>());
        project1.setUserLikes(new ArrayList<>());
        project1.setFavoriteProjects(new ArrayList<>());
        project1.setProjectFollowers(new ArrayList<>());
        Project project2 = new Project();
        project2.setId(2L);
        project2.setMediaList(new ArrayList<>());
        project1.setComments(new ArrayList<>());
        project2.setComments(new ArrayList<>());
        project2.setUserLikes(new ArrayList<>());
        project2.setFavoriteProjects(new ArrayList<>());
        project2.setProjectFollowers(new ArrayList<>());
        user.setFavoriteProjects(Arrays.asList(project1, project2));

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<List<ProjectDTO>> favoriteProjectsOptional = likesAndFavoritesService.getFavoriteProjects(1L);

        assertTrue(favoriteProjectsOptional.isPresent());
        List<ProjectDTO> favoriteProjects = favoriteProjectsOptional.get();
        assertEquals(2, favoriteProjects.size());
        assertTrue(favoriteProjects.stream().anyMatch(projectDTO -> projectDTO.getId() == 1L));
        assertTrue(favoriteProjects.stream().anyMatch(projectDTO -> projectDTO.getId() == 2L));
    }


    @Test
    void getLikedProjects_Success() {
        Users user = new Users();
        user.setId(1L);
        user.setLikedProjects(new ArrayList<>());

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<List<ProjectDTO>> projectDTOsOptional = likesAndFavoritesService.getLikedProjects(1L);

        assertTrue(projectDTOsOptional.isPresent());
        assertTrue(projectDTOsOptional.get().isEmpty());
    }

}
