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
    private String user;
    private String title;
    private String description;
    private String content;
    private String category;
    private String complexity;
    private List<Media> mediaList;
}
