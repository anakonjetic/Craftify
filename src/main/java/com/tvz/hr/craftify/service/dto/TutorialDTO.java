package com.tvz.hr.craftify.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorialDTO {
    private Long id;
    private String title;
    private String content;
    private UserDTO user;
    private CategoryDTO category;
    private ComplexityDTO complexity;
    private List<MediaDTO> mediaList;
}
