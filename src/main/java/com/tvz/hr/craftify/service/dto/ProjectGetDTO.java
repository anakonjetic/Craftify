package com.tvz.hr.craftify.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectGetDTO {
    private Long id;
    private String title;
    private String description;
    private String content;
    private UserDTO user;
    private CategoryDTO category;
    private ComplexityDTO complexity;
}
