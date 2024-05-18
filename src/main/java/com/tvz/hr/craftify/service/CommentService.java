package com.tvz.hr.craftify.service;
import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.service.dto.CommentDTO;
import com.tvz.hr.craftify.service.dto.CommentPostPutDTO;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDTO> getAllComments();
    Optional<CommentDTO> getCommentById(long id);
    CommentDTO createComment(CommentPostPutDTO comment);
    CommentDTO updateComment(CommentPostPutDTO comment, long id);
    void deleteComment(long id);
    List<CommentDTO> getCommentsByProject(long projectId);
}
