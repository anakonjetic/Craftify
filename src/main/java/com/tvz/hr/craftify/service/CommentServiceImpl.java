package com.tvz.hr.craftify.service;
import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.repository.CommentRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.CommentDTO;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private UsersRepository usersRepository;

    @Override
    public List<CommentDTO> getAllComments(long id) {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(MapToDTOHelper::mapToCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CommentDTO> getCommentById(long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.map(MapToDTOHelper::mapToCommentDTO);
    }

    @Override
    public Comment createComment(Comment comment) {
        Comment newComment = commentRepository.save(comment);
        return newComment;
    }

    @Override
    public Comment updateComment(Comment comment, long id) {
        Optional<Comment> commentOptional = commentRepository.findById(comment.getId());
        if (commentOptional.isPresent()) {
            Comment commentToUpdate = commentOptional.get();
            commentToUpdate.setComment(comment.getComment());
            return commentRepository.save(commentToUpdate);
        }
        else {
            throw new EntityNotFoundException("Comment with id " + comment.getId() + " not found");
        }
    }

    @Override
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> getCommentsByProject(long projectId) {
        return commentRepository.findByProjectId(projectId);
    }
}
