package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.RefreshToken;
import com.tvz.hr.craftify.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class RefreshTokenRepositoryTest {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UsersRepository usersRepository;
    private Users testUser;

    @BeforeEach
    public void setUp() {
        testUser = usersRepository.findById(1L).get();
        RefreshToken token = new RefreshToken();
        token.setToken("30cd128d-6f93-488d-ae45-1808d5eaecbe");
        token.setUser(testUser);
        refreshTokenRepository.save(token);
    }
    @Test
    public void testFindByToken(){
        Optional<RefreshToken> token = refreshTokenRepository.findByToken("30cd128d-6f93-488d-ae45-1808d5eaecbe");
        assertTrue(token.isPresent());
        assertEquals(testUser,token.get().getUser());
    }

    @Test
    public void testFindByUser_id(){
        Optional<RefreshToken> token = refreshTokenRepository.findByUser_id(1L);
        assertTrue(token.isPresent());
        assertEquals("30cd128d-6f93-488d-ae45-1808d5eaecbe", token.get().getToken());
    }
}
