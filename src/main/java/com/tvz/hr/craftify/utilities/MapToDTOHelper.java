package com.tvz.hr.craftify.utilities;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.service.*;

import java.util.stream.Collectors;

public class MapToDTOHelper {

    //TODO usera kreatora namapirati
    public static ProjectDTO mapToProjectDTO(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getContent(),
                project.getCategory(),
                project.getComplexity(),
                project.getMediaList().stream().map(MapToDTOHelper::mapToMediaDTO).collect(Collectors.toList()),
                project.getComments().stream().map(MapToDTOHelper::mapToCommentDTO).collect(Collectors.toList()),
                project.getUserLikes().stream().map(MapToDTOHelper::mapToUserDTO).collect(Collectors.toList()),
                project.getFavoriteProjects().stream().map(MapToDTOHelper::mapToUserDTO).collect(Collectors.toList()),
                project.getProjectFollowers().stream().map(MapToDTOHelper::mapToUserDTO).collect(Collectors.toList())
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

    public static Category mapToCategory(CategoryDTO categoryDTO){
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        return category;
    }

}
