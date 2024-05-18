package com.tvz.hr.craftify.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsPostPutDTO {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private String imageUrl;
}