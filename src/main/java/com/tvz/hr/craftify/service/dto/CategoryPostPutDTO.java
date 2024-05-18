package com.tvz.hr.craftify.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPostPutDTO {
    private Long id;
    private String name;
    List<Long> projectIdList;
    List<Long> tutorialIdList;
}
