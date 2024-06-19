package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/data.sql")
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testGetById() {
        Optional<Project> project = projectRepository.findById(1L);
        assertTrue(project.isPresent());
        assertEquals("DIY Mason Jar Lanterns", project.get().getTitle());
    }

    @Test
    public void testFindByCategory_Id() {
        List<Project> projects = projectRepository.findByCategory_Id(1L);
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertEquals("DIY Mason Jar Lanterns", projects.getFirst().getTitle());
    }

    @Test
    public void testFindByComplexity_Id() {
        List<Project> projects = projectRepository.findByComplexity_Id(2L);
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertEquals("DIY Fabric Wall Art", projects.getFirst().getTitle());
    }

    @Test
    public void testFindByUser_Id() {
        List<Project> projects = projectRepository.findByUser_id(1L);
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertEquals("DIY Mason Jar Lanterns", projects.get(0).getTitle());
    }

    @Test
    public void testFindByFilters() {
        List<Project> projects = projectRepository.findByFilters("Mason", null, null);
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertEquals("DIY Mason Jar Lanterns", projects.getFirst().getTitle());

        projects = projectRepository.findByFilters(null, 3L, null);
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertEquals("DIY Fabric Wall Art", projects.getFirst().getTitle());

        projects = projectRepository.findByFilters(null, null, 1L);
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertEquals("DIY Mason Jar Lanterns", projects.getFirst().getTitle());
    }

    @Test
    public void testGetAllProjectsAndOrderByUserLikes() {
        List<Project> projects = projectRepository.getAllProjectsAndOrderByUserLikes();
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertEquals(2L, projects.getFirst().getId());
    }

    @Test
    public void testGetAllProjectsByCategoryIdAndOrderByUserLikes() {
        Long categoryId = 1L;
        List<Project> projects = projectRepository.getAllProjectsByCategoryIdAndOrderByUserLikes(categoryId);
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
    }

    @Test
    public void testDeleteFavoritesByProjectId() {
        projectRepository.deleteFavoritesByProjectId(1L);
    }

    @Test
    public void testDeleteProjectSubscribersByProjectId() {
        projectRepository.deleteProjectSubscribersByProjectId(1L);
    }

    @Test
    public void testDeleteUserProjectLikesByProjectId() {
        projectRepository.deleteUserProjectLikesByProjectId(1L);
    }
}
