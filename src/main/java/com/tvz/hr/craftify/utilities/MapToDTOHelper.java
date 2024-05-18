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

    public static TutorialDTO mapToTutorialDTO(Tutorial tutorial){
        List<MediaDTO> mediaDTO = tutorial.getMediaList() != null ?
                tutorial.getMediaList().stream().map(MapToDTOHelper::mapToMediaDTO).collect(Collectors.toList()) : null;

        return new TutorialDTO(
                tutorial.getId(),
                tutorial.getTitle(),
                tutorial.getContent(),
                MapToDTOHelper.mapToUserDTO(tutorial.getUser()),
                mapToCategoryDTO(tutorial.getCategory()),
                mapToComplexityDTO(tutorial.getComplexity()),
                mediaDTO
        );
    }

    public static UserDTO mapToUserDTO(Users user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
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

    public static MediaGetDTO mapToMediaGetDTO(Media media) {
        ProjectGetDTO projectDTO = media.getProject() != null ? mapToProjectGetDTO(media.getProject()) : null;
        TutorialDTO tutorialDTO = media.getTutorial() != null ? mapToTutorialDTO(media.getTutorial()) : null;

        return new MediaGetDTO(
                media.getId(),
                media.getMedia(),
                media.getMediaOrder(),
                projectDTO,
                tutorialDTO
        );
    }

    public static CommentDTO mapToCommentDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getComment(),
                mapToUserDTO(comment.getUser()),
                mapToProjectGetDTO(comment.getProject()),
                comment.getCommentTime()
        );
    }

    public static CategoryDTO mapToCategoryDTO(Category category){
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }

    public static CategoryGetDTO mapToCategoryGetDTO(Category category){
        List<UserDTO> users = category.getUserPreferences() != null ?
                category.getUserPreferences().stream()
                        .map(MapToDTOHelper::mapToUserDTO).collect(Collectors.toList()) : null;

        List<ProjectGetDTO> projects = category.getProjectList() != null ?
                category.getProjectList().stream()
                        .map(MapToDTOHelper::mapToProjectGetDTO).collect(Collectors.toList()) : null;

        List<TutorialDTO> tutorials = category.getTutorialList() != null ?
                category.getTutorialList().stream()
                        .map(MapToDTOHelper::mapToTutorialDTO).collect(Collectors.toList()): null;

        return new CategoryGetDTO(
                category.getId(),
                category.getName(),
                users,
                projects,
                tutorials
        );
    }
    public static ComplexityDTO mapToComplexityDTO(Complexity complexity){
        return new ComplexityDTO(
                complexity.getId(),
                complexity.getName()
        );
    }

    public static ComplexityGetDTO mapToComplexityGetDTO(Complexity complexity){
        List<Project> projects = complexity.getProjectList() != null ? complexity.getProjectList() : null;
        List<ProjectGetDTO> projectDTO = projects != null ? projects.stream()
                .map(MapToDTOHelper::mapToProjectGetDTO).collect(Collectors.toList()) : null;

        return new ComplexityGetDTO(
                complexity.getId(),
                complexity.getName(),
                projectDTO
        );
    }

    public static UsersGetDTO mapToUsersGetDTO(Users user) {
        /*List<String> categoryNames = user.getUserPreferences().stream()
                .map(Category::getName)
                .collect(Collectors.toList());*/
        List<CategoryDTO> category = user.getUserPreferences() != null ? user.getUserPreferences().stream()
                .map(MapToDTOHelper::mapToCategoryDTO)
                .collect(Collectors.toList()) : null;

        return new UsersGetDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isAdmin(),
                category
        );
    }

    }
