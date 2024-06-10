package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@Sql("/data.sql")
public class UsersRepositoryTest {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private Users testUser;
    private Users testUser2;
    private Project testProject;
    private Project testProject2;

    @Test
    public void testFindFirstByUsernameOrEmailIgnoreCase(){
        Optional<Users> user = usersRepository.getFirstByUsernameOrEmailIgnoreCase("john_doe","john@example.com");
        assertTrue(user.isPresent());
        assertEquals("John Doe", user.get().getName());
    }

    @Test
    public void testFindByUsername(){
        Users user = usersRepository.findByUsername("john_doe");
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
    }

    @BeforeEach
    public void setUp() {
        testUser = usersRepository.findById(1L).get();
        testUser2 = usersRepository.findById(2L).get();
        testProject = projectRepository.findById(1L).get();
        testProject2 = projectRepository.findById(2L).get();
    }

    @Test
    public void testDeleteProjectSubscribersByUserId() {
        usersRepository.deleteProjectSubscribersByUserId(testUser2.getId());
        assertEquals(0L,testProject2.getProjectFollowers().size());
    }

    @Test
    public void testDeleteUserProjectLikesByProjectUserId() {
        usersRepository.deleteUserProjectLikesByProjectUserId(testUser2.getId());
        assertEquals(0L,testProject2.getUserLikes().size());
    }
    @Test
    public void testDeleteFavoritesByProjectUserId() {
        usersRepository.deleteFavoritesByProjectUserId(testUser2.getId());
        assertEquals(0L,testProject2.getFavoriteProjects().size());
    }

    @Test
    public void testDeleteChildCommentsByProjectUserId() {
        usersRepository.deleteChildCommentsByProjectUserId(testUser2.getId());
        assertEquals(1L, testProject2.getComments().size());
        assertEquals(0L, testProject2.getComments().getFirst().getChildComments().size());
    }

    @Test
    public void testDeleteCommentsByProjectUserId() {
        usersRepository.deleteChildCommentsByProjectUserId(testUser2.getId());
        usersRepository.deleteCommentsByProjectUserId(testUser2.getId());
        assertEquals(0L, testProject2.getComments().size());
    }

    @Test
    public void testDeleteProjectSubscribersByUserIdOnly() {
        usersRepository.deleteProjectSubscribersByUserIdOnly(testUser.getId());
        assertEquals(0L, testProject2.getProjectFollowers().size());
    }

    @Test
    public void testDeleteUserProjectLikesByUserId() {
        usersRepository.deleteUserProjectLikesByUserId(testUser.getId());
        assertEquals(0L, testProject2.getUserLikes().size());
    }

    @Test
    public void testDeleteUserSubscribersByUserId() {
        usersRepository.deleteUserSubscribersByUserId(testUser.getId());
        assertEquals(0L, testUser2.getFollowers().size());
        assertEquals(0L, testUser.getFollowedUsers().size());
    }

    @Test
    public void testDeleteMediaProjectsByUserId() {
        usersRepository.deleteMediaProjectsByUserId(testUser.getId());
        assertEquals(0L, testProject.getMediaList().size());
    }

    @Test
    public void testDeleteProjectsByUserId() {
        usersRepository.deleteProjectSubscribersByUserId(testUser2.getId());
        usersRepository.deleteUserProjectLikesByProjectUserId(testUser2.getId());
        usersRepository.deleteFavoritesByProjectUserId(testUser2.getId());
        usersRepository.deleteProjectSubscribersByUserIdOnly(testUser2.getId());
        usersRepository.deleteUserProjectLikesByUserId(testUser2.getId());
        usersRepository.deleteChildCommentsByProjectUserId(testUser2.getId());
        usersRepository.deleteCommentsByProjectUserId(testUser2.getId());
        usersRepository.deleteMediaProjectsByUserId(testUser2.getId());
        usersRepository.deleteProjectsByUserId(testUser2.getId());
        assertEquals(0L, testUser2.getProjects().size());
    }

    @Test
    public void testDeleteUserPreferencesByUserId() {
        usersRepository.deleteUserPreferencesByUserId(testUser.getId());
        assertEquals(0L, testUser.getUserPreferences().size());
    }

    @Test
    public void testDeleteFavoritesByUserId() {
        usersRepository.deleteFavoritesByUserId(testUser2.getId());
        assertEquals(0L, testProject.getFavoriteProjects().size());
    }

    @Test
    public void testDeleteRefreshTokensByUserId() {
        RefreshToken token = new RefreshToken();
        token.setToken("DHJAEFIUBA");
        token.setUser(testUser);
        refreshTokenRepository.save(token);
        Optional<RefreshToken> initialRefreshToken = refreshTokenRepository.findByUser_id(testUser.getId());
        assertTrue(initialRefreshToken.isPresent());
        usersRepository.deleteRefreshTokensByUserId(testUser.getId());
        Optional<RefreshToken> finalRefreshToken = refreshTokenRepository.findByUser_id(testUser.getId());
        assertTrue(finalRefreshToken.isEmpty());
    }
}
