package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private String content;
    private Category category;
    private Complexity complexity;
    private List<MediaDTO> mediaList;
    private List<CommentDTO> comments;
    private List<UserDTO> userLikes;
    private List<UserDTO> favoriteProjects;
    private List<UserDTO> projectFollowers;
}
