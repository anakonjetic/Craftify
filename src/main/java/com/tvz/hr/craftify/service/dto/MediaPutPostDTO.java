package com.tvz.hr.craftify.service.dto;

import com.tvz.hr.craftify.model.Tutorial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaPutPostDTO {
    private Long id;
    private String media;
    private Integer mediaOrder;
    private Long projectId;
    private Long tutorialId;
}
