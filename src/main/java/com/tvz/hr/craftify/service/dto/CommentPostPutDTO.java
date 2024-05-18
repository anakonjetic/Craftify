package com.tvz.hr.craftify.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentPostPutDTO {
    private Long id;
    private String comment;
    private Long userId;
    private Long projectId;
    private Long parentCommentId;
}
