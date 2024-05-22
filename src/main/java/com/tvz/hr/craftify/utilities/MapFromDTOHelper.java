package com.tvz.hr.craftify.utilities;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.service.dto.*;

import java.util.stream.Collectors;

public class MapFromDTOHelper {
    public static Project mapProjectDTOToProject(ProjectDTO project) {
        return new Project(
                project.getId(),
                mapUserDTOToUsers(project.getUser()),
                project.getTitle(),
                project.getDescription(),
                project.getContent(),
                mapCategoryDTOToCategory(project.getCategory()),
                mapComplexityDTOToComplexity(project.getComplexity()),
                project.getMediaList().stream().map(MapFromDTOHelper::mapMediaDTOToMedia).collect(Collectors.toList()),
                project.getUserLikes().stream().map(MapFromDTOHelper::mapUserDTOToUsers).collect(Collectors.toList()),
                project.getFavoriteProjects().stream().map(MapFromDTOHelper::mapUserDTOToUsers).collect(Collectors.toList()),
                project.getProjectFollowers().stream().map(MapFromDTOHelper::mapUserDTOToUsers).collect(Collectors.toList()),
                project.getComments().stream().map(MapFromDTOHelper::mapCommentDTOToComment).collect(Collectors.toList())
        );
    }
    public static Project mapProjectGetDTOToProject(ProjectGetDTO project){
        return new Project(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getContent(),
                mapUserDTOToUsers(project.getUser()),
                mapCategoryDTOToCategory(project.getCategory()),
                mapComplexityDTOToComplexity(project.getComplexity())
        );
    }

    public static Complexity mapComplexityGetDTOToComplexity(ComplexityGetDTO complexity){
        return new Complexity(
                complexity.getId(),
                complexity.getName(),
                complexity.getProjects().stream().map(MapFromDTOHelper::mapProjectGetDTOToProject).collect(Collectors.toList())
        );
    }

    public static Complexity mapComplexityDTOToComplexity(ComplexityDTO complexity){
        return new Complexity(
                complexity.getId(),
                complexity.getName()
        );
    }
    public static Category mapCategoryDTOToCategory(CategoryDTO category){
        return new Category(
                category.getId(),
                category.getName()
        );
    }

    public static News mapNewsDTOToNews(NewsDTO newsDTO) {
        News news = new News();
        news.setId(newsDTO.getId());
        news.setTitle(newsDTO.getTitle());
        news.setContent(newsDTO.getContent());
        Category category = new Category();
        category.setId(newsDTO.getCategory().getId());
        news.setCategory(category);
        news.setImageUrl(newsDTO.getImageUrl());
        return news;
    }

    public static Category mapCategoryGetDTOToCategoryDTO(CategoryGetDTO category){
        return new Category(
                category.getId(),
                category.getName(),
                category.getUsers().stream().map(MapFromDTOHelper::mapUserDTOToUsers).collect(Collectors.toList()),
                category.getProjects().stream().map(MapFromDTOHelper::mapProjectGetDTOToProject).collect(Collectors.toList()),
                category.getTutorials().stream().map(MapFromDTOHelper::mapTutorialDTOToTutorial).collect(Collectors.toList())
        );
    }

    public static Users mapUsersGetDTOToUsers(UsersGetDTO user){
        return new Users(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isAdmin(),
                user.isPrivate(),
                user.getUserPreferences().stream().map(MapFromDTOHelper::mapCategoryDTOToCategory).collect(Collectors.toList())
        );
    }

    public static Users mapUserDTOToUsers(UserDTO user){
        return new Users(
                user.getId(),
                user.getName(),
                user.getUsername()
        );
    }

    public static Tutorial mapTutorialDTOToTutorial(TutorialDTO tutorial){
        return new Tutorial(
                tutorial.getId(),
                tutorial.getTitle(),
                tutorial.getContent(),
                mapUserDTOToUsers(tutorial.getUser()),
                mapCategoryDTOToCategory(tutorial.getCategory()),
                mapComplexityDTOToComplexity(tutorial.getComplexity()),
                tutorial.getMediaList().stream().map(MapFromDTOHelper::mapMediaDTOToMedia).collect(Collectors.toList())
        );
    }
    public static Media mapMediaDTOToMedia(MediaDTO media){
        return new Media(
                media.getId(),
                media.getMedia(),
                media.getMediaOrder()
        );
    }

    public static Media mapMediaGetDTOToMedia(MediaGetDTO media){
        return new Media(
                media.getId(),
                media.getMedia(),
                media.getMediaOrder(),
                mapProjectGetDTOToProject(media.getProject()),
                mapTutorialDTOToTutorial(media.getTutorial())
        );
    }

    public static Comment mapCommentDTOToComment(CommentDTO comment){
        return new Comment(
                comment.getId(),
                comment.getComment(),
                mapUserDTOToUsers(comment.getUser()),
                mapProjectGetDTOToProject(comment.getProject()),
                comment.getCommentTime()
        );
    }
}
