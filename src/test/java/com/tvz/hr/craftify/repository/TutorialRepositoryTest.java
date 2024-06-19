package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.model.Complexity;
import com.tvz.hr.craftify.model.Tutorial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TutorialRepositoryTest {
    @Autowired
    private TutorialRepository tutorialRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ComplexityRepository complexityRepository;

    private Category category;
    private Complexity complexity;

    @BeforeEach
    public void setUp(){
        category = categoryRepository.getById(1L);
        complexity = complexityRepository.getById(1L);
    }

    @Test
    public void testFindByCategory(){
        List<Tutorial> tutorialList = tutorialRepository.findByCategory(category);
        assertNotNull(tutorialList);
        assertFalse(tutorialList.isEmpty());
        assertEquals(1L, tutorialList.getFirst().getId());
    }

    @Test
    public void testFindByComplexity(){
        List<Tutorial> tutorialList = tutorialRepository.findByComplexity(complexity);
        assertNotNull(tutorialList);
        assertFalse(tutorialList.isEmpty());
        assertEquals(1L, tutorialList.getFirst().getId());
    }
    @Test
    public void testFindByCategory_Id(){
        List<Tutorial> tutorialList = tutorialRepository.findByCategory_Id(1L);
        assertNotNull(tutorialList);
        assertFalse(tutorialList.isEmpty());
        assertEquals("DIY Paper Flowers",tutorialList.getFirst().getTitle());
    }
}
