package com.tvz.hr.craftify.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryGetDTO {
    private Long id;
    private String name;
    private List<UserDTO> users;
    private List<ProjectGetDTO> projects;
    private List<TutorialDTO> tutorials;
}
