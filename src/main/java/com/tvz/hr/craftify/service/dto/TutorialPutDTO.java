package com.tvz.hr.craftify.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorialPutDTO {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private Long complexityId;
}