package com.tvz.hr.craftify.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaGetDTO {
    private Long id;
    private String media;
    private Integer mediaOrder;
    private ProjectDTO project;
    private TutorialDTO tutorial;
}
