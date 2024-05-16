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
    private ProjectGetDTO project;
    private TutorialDTO tutorial;
}
