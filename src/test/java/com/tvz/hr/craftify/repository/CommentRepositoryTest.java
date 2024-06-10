package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/data.sql")
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testFindByProjectId() {
        Long projectId = 1L;

        List<Comment> comments = commentRepository.findByProjectId(projectId);

        assertNotNull(comments);
        assertFalse(comments.isEmpty());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;

        Optional<List<Comment>> comments = commentRepository.findByUserId(userId);

        assertTrue(comments.isPresent());
        assertFalse(comments.get().isEmpty());
    }
}
