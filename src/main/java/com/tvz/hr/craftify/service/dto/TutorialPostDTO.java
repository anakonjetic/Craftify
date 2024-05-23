package com.tvz.hr.craftify.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorialPostDTO {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Long categoryId;
    private Long complexityId;
    private List<Long> comments;
    private List<Long> mediaList;
}
