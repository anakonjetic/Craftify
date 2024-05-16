package com.tvz.hr.craftify.service;
import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.service.dto.CommentDTO;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDTO> getAllComments(long id);
    Optional<CommentDTO> getCommentById(long id);
    Comment createComment(Comment comment);
    Comment updateComment(Comment comment, long id);
    void deleteComment(long id);
    List<Comment> getCommentsByProject(long projectId);
}
