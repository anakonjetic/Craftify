package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.service.dto.SubscriptionDTO;
import com.tvz.hr.craftify.service.dto.UserDTO;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SubscriptionServiceImplTest {

    private SubscriptionServiceImpl subscriptionService;
    private UsersRepository usersRepository;
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        usersRepository = mock(UsersRepository.class);
        projectRepository = mock(ProjectRepository.class);

        subscriptionService = new SubscriptionServiceImpl(usersRepository, projectRepository);
    }

    @Test
    void getUserFollowers_Success() {
        Users user = new Users();
        user.setId(1L);
        user.setFollowers(new ArrayList<>());
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<List<UserDTO>> userFollowers = subscriptionService.getUserFollowers(1L);

        assertEquals(0, userFollowers.orElse(new ArrayList<>()).size());
    }

    @Test
    void getUserFollowings_Success() {
        Users user = new Users();
        user.setId(1L);
        user.setFollowedUsers(new ArrayList<>());
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<List<UserDTO>> userFollowings = subscriptionService.getUserFollowings(1L);

        assertEquals(0, userFollowings.orElse(new ArrayList<>()).size());
    }

    @Test
    void getUserProjectFollowings_Success() {
        Users user = new Users();
        user.setId(1L);
        user.setFollowingProjects(new ArrayList<>());
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<List<ProjectDTO>> projectFollowings = subscriptionService.getUserProjectFollowings(1L);

        assertEquals(0, projectFollowings.orElse(new ArrayList<>()).size());
    }

    @Test
    void getProjectFollowers_Success() {
        Project project = new Project();
        project.setId(1L);
        project.setProjectFollowers(new ArrayList<>());
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<List<UserDTO>> projectFollowers = subscriptionService.getProjectFollowers(1L);

        assertEquals(0, projectFollowers.orElse(new ArrayList<>()).size());
    }

    @Test
    void followUser_Success() {
        SubscriptionDTO sub = new SubscriptionDTO();
        sub.setUserId(3L);
        sub.setFollowingId(4L);

        Users user = new Users();
        user.setId(3L);
        Users followedUser = new Users();
        followedUser.setId(4L);
        List<Users> followingUsers = new ArrayList<>();
        user.setFollowedUsers(followingUsers);

        when(usersRepository.findById(3L)).thenReturn(Optional.of(user));
        when(usersRepository.findById(4L)).thenReturn(Optional.of(followedUser));

        subscriptionService.followUser(sub);

        assertTrue(user.getFollowedUsers().contains(followedUser));
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    void unfollowUser_Success() {
        SubscriptionDTO sub = new SubscriptionDTO();
        sub.setUserId(1L);
        sub.setFollowingId(2L);

        Users user = new Users();
        user.setId(1L);
        Users followedUser = new Users();
        followedUser.setId(2L);
        List<Users> followingUsers = new ArrayList<>();
        followingUsers.add(followedUser);
        user.setFollowedUsers(followingUsers);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(usersRepository.findById(2L)).thenReturn(Optional.of(followedUser));

        subscriptionService.unfollowUser(sub);

        verify(usersRepository, times(1)).save(user);
    }

    @Test
    void followProject_Success() {
        SubscriptionDTO sub = new SubscriptionDTO();
        sub.setUserId(3L);
        sub.setFollowingId(4L);

        Users user = new Users();
        user.setId(3L);
        Project followedProject = new Project();
        followedProject.setId(4L);
        List<Project> followingProjects = new ArrayList<>();
        user.setFollowingProjects(followingProjects);

        when(usersRepository.findById(3L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(4L)).thenReturn(Optional.of(followedProject));

        subscriptionService.followProject(sub);

        assertTrue(user.getFollowingProjects().contains(followedProject));
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    void unfollowProject_Success() {
        SubscriptionDTO sub = new SubscriptionDTO();
        sub.setUserId(1L);
        sub.setFollowingId(2L);

        Users user = new Users();
        user.setId(1L);
        Project project = new Project();
        project.setId(2L);
        List<Project> followingProjects = new ArrayList<>();
        followingProjects.add(project);
        user.setFollowingProjects(followingProjects);

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(2L)).thenReturn(Optional.of(project));

        subscriptionService.unfollowProject(sub);

        verify(usersRepository, times(1)).save(user);
    }
}
