package com.tvz.hr.craftify.utilities;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.service.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public class MapToDTOHelper {

    //TODO usera kreatora namapirati
    public static ProjectDTO mapToProjectDTO(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getContent(),
                MapToDTOHelper.mapToUserDTO(project.getUser()),
                mapToCategoryDTO(project.getCategory()),
                mapToComplexityDTO(project.getComplexity()),
                project.getMediaList().stream().map(MapToDTOHelper::mapToMediaDTO).collect(Collectors.toList()),
                project.getComments().stream().map(MapToDTOHelper::mapToCommentDTO).collect(Collectors.toList()),
                project.getUserLikes().stream().map(MapToDTOHelper::mapToUserDTO).collect(Collectors.toList()),
                project.getFavoriteProjects().stream().map(MapToDTOHelper::mapToUserDTO).collect(Collectors.toList()),
                project.getProjectFollowers().stream().map(MapToDTOHelper::mapToUserDTO).collect(Collectors.toList())
        );
    }

    public static ProjectGetDTO mapToProjectGetDTO(Project project) {
        return new ProjectGetDTO(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getContent(),
                MapToDTOHelper.mapToUserDTO(project.getUser()),
                mapToCategoryDTO(project.getCategory()),
                mapToComplexityDTO(project.getComplexity())
        );
    }

    public static UserDTO mapToUserDTO(Users user) {
        return new UserDTO(
                user.getId(),
                user.getUsername()
        );
    }

    public static MediaDTO mapToMediaDTO(Media media) {
        return new MediaDTO(
                media.getId(),
                media.getMedia(),
                media.getMediaOrder()
        );
    }

    public static CommentDTO mapToCommentDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getComment(),
                mapToUserDTO(comment.getUser()),
                comment.getCommentTime()
        );
    }

    public static CategoryDTO mapToCategoryDTO(Category category){
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }
    public static ComplexityDTO mapToComplexityDTO(Complexity complexity){
        return new ComplexityDTO(
                complexity.getId(),
                complexity.getName()
        );
    }

    public static ComplexityGetDTO mapToComplexityGetDTO(Complexity complexity){
        return new ComplexityGetDTO(
                complexity.getId(),
                complexity.getName(),
                complexity.getProjectList().stream().map(MapToDTOHelper::mapToProjectDTO).collect(Collectors.toList())
        );
    }
    public static TutorialDTO mapToTutorialDTO(Tutorial tutorial){
        return new TutorialDTO(
                tutorial.getId(),
                tutorial.getTitle(),
                tutorial.getContent(),
                mapToCategoryDTO(tutorial.getCategory()),
                mapToComplexityDTO(tutorial.getComplexity())
        );
    }

    public static UsersGetDTO mapToUsersGetDTO(Users user) {
        /*List<String> categoryNames = user.getUserPreferences().stream()
                .map(Category::getName)
                .collect(Collectors.toList());*/
        List<CategoryDTO> category = user.getUserPreferences().stream()
                .map(MapToDTOHelper::mapToCategoryDTO)
                .collect(Collectors.toList());

        return new UsersGetDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isAdmin(),
                category
        );
    }

    }
