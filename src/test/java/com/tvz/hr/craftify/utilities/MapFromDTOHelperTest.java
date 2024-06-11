package com.tvz.hr.craftify.utilities;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.service.dto.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MapFromDTOHelperTest {

    @Test
    void mapProjectDTOToProject() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);

        ComplexityDTO complexityDTO = new ComplexityDTO();
        complexityDTO.setId(1L);

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setTitle("Project Title");
        projectDTO.setUser(userDTO);
        projectDTO.setCategory(categoryDTO);
        projectDTO.setComplexity(complexityDTO);
        projectDTO.setMediaList(new ArrayList<>());
        projectDTO.setUserLikes(new ArrayList<>());
        projectDTO.setFavoriteProjects(new ArrayList<>());
        projectDTO.setProjectFollowers(new ArrayList<>());
        projectDTO.setComments(new ArrayList<>());

        Project project = MapFromDTOHelper.mapProjectDTOToProject(projectDTO);

        assertNotNull(project);
        assertEquals(projectDTO.getId(), project.getId());
        assertEquals(projectDTO.getTitle(), project.getTitle());
    }

    @Test
    void mapComplexityGetDTOToComplexity() {

        ComplexityGetDTO complexityDTO = new ComplexityGetDTO();
        complexityDTO.setId(1L);
        complexityDTO.setName("Complexity");
        complexityDTO.setProjects(new ArrayList<>());

        Complexity complexity = MapFromDTOHelper.mapComplexityGetDTOToComplexity(complexityDTO);

        assertNotNull(complexity);
        assertEquals(complexityDTO.getId(), complexity.getId());
        assertEquals(complexityDTO.getName(), complexity.getName());
    }

    @Test
    void mapCategoryDTOToCategory() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Category");

        Category category = MapFromDTOHelper.mapCategoryDTOToCategory(categoryDTO);

        assertNotNull(category);
        assertEquals(categoryDTO.getId(), category.getId());
        assertEquals(categoryDTO.getName(), category.getName());
    }

    @Test
    void mapNewsDTOToNews() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(1L);
        newsDTO.setTitle("News Title");
        newsDTO.setContent("News Content");
        newsDTO.setImageUrl("image-url");

        News news = MapFromDTOHelper.mapNewsDTOToNews(newsDTO);

        assertNotNull(news);
        assertEquals(newsDTO.getId(), news.getId());
        assertEquals(newsDTO.getTitle(), news.getTitle());
        assertEquals(newsDTO.getContent(), news.getContent());
        assertEquals(newsDTO.getImageUrl(), news.getImageUrl());
    }

    @Test
    void mapUsersGetDTOToUsers() {
        UsersGetDTO usersGetDTO = new UsersGetDTO();
        usersGetDTO.setId(1L);
        usersGetDTO.setName("John Doe");
        usersGetDTO.setUserPreferences(new ArrayList<>());

        Users user = MapFromDTOHelper.mapUsersGetDTOToUsers(usersGetDTO);

        assertNotNull(user);
        assertEquals(usersGetDTO.getId(), user.getId());
        assertEquals(usersGetDTO.getName(), user.getName());
    }

    @Test
    void mapUserDTOToUsers() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");

        Users user = MapFromDTOHelper.mapUserDTOToUsers(userDTO);

        assertNotNull(user);
        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getName(), user.getName());
    }

    @Test
    void mapTutorialDTOToTutorial() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Category");

        ComplexityDTO complexityDTO = new ComplexityDTO();
        complexityDTO.setId(1L);
        complexityDTO.setName("Complexity");

        TutorialDTO tutorialDTO = new TutorialDTO();
        tutorialDTO.setId(1L);
        tutorialDTO.setTitle("Tutorial Title");
        tutorialDTO.setContent("Tutorial Content");
        tutorialDTO.setUser(userDTO);
        tutorialDTO.setCategory(categoryDTO);
        tutorialDTO.setComplexity(complexityDTO);
        tutorialDTO.setMediaList(new ArrayList<>());

        Tutorial tutorial = MapFromDTOHelper.mapTutorialDTOToTutorial(tutorialDTO);

        assertNotNull(tutorial);
        assertEquals(tutorialDTO.getId(), tutorial.getId());
        assertEquals(tutorialDTO.getTitle(), tutorial.getTitle());
        assertEquals(tutorialDTO.getContent(), tutorial.getContent());
    }

    @Test
    void mapMediaDTOToMedia() {
        MediaDTO mediaDTO = new MediaDTO();
        mediaDTO.setId(1L);
        mediaDTO.setMedia("Media Content");
        mediaDTO.setMediaOrder(1);

        Media media = MapFromDTOHelper.mapMediaDTOToMedia(mediaDTO);

        assertNotNull(media);
        assertEquals(mediaDTO.getId(), media.getId());
        assertEquals(mediaDTO.getMedia(), media.getMedia());
        assertEquals(mediaDTO.getMediaOrder(), media.getMediaOrder());
    }

    @Test
    void mapCommentDTOToComment() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");

        ComplexityDTO complexityDTO = new ComplexityDTO();
        complexityDTO.setId(1L);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Category");

        ProjectGetDTO projectDTO = new ProjectGetDTO();
        projectDTO.setId(1L);
        projectDTO.setTitle("Project Title");
        projectDTO.setUser(userDTO);
        projectDTO.setCategory(categoryDTO);
        projectDTO.setComplexity(complexityDTO);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setComment("Comment Content");
        commentDTO.setUser(userDTO);
        commentDTO.setProject(projectDTO);

        Comment comment = MapFromDTOHelper.mapCommentDTOToComment(commentDTO);

        assertNotNull(comment);
        assertEquals(commentDTO.getId(), comment.getId());
        assertEquals(commentDTO.getComment(), comment.getComment());
    }
}

