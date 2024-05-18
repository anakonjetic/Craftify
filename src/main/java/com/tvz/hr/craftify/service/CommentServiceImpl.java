package com.tvz.hr.craftify.service;
import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.CommentRepository;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.CommentDTO;
import com.tvz.hr.craftify.service.dto.CommentPostPutDTO;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tvz.hr.craftify.utilities.MapToDTOHelper.mapToCommentDTO;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private UsersRepository usersRepository;
    private ProjectRepository projectRepository;

    @Override
    public List<CommentDTO> getAllComments() {
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
    public CommentDTO createComment(CommentPostPutDTO comment) {
        Long parentCommentId = comment.getParentCommentId() != null ? comment.getParentCommentId() : null;
        Comment parentComment = parentCommentId != null ? commentRepository.getById(parentCommentId) : null;
        Users user = usersRepository.getById(comment.getUserId());
        Project project = projectRepository.getById(comment.getProjectId());

        Comment newComment = new Comment(
                comment.getId(),
                comment.getComment(),
                user,
                project,
                parentComment,
                LocalDateTime.now()
        );
        return mapToCommentDTO(commentRepository.save(newComment));
    }

    @Override
    public CommentDTO updateComment(CommentPostPutDTO comment, long id) {
        Optional<Comment> commentOptional = commentRepository.findById(comment.getId());
        if (commentOptional.isPresent()) {
            Comment commentToUpdate = commentOptional.get();
            Long parentCommentId = comment.getParentCommentId() != null ? comment.getParentCommentId() : null;
            Comment parentComment = parentCommentId != null ? commentRepository.getById(parentCommentId) : null;

            commentToUpdate.setComment(comment.getComment());
            commentToUpdate.setUser(usersRepository.getById(comment.getUserId()));
            commentToUpdate.setProject(projectRepository.getById(comment.getProjectId()));
            commentToUpdate.setParentComment(parentComment);

            return mapToCommentDTO(commentRepository.save(commentToUpdate));
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
    public List<CommentDTO> getCommentsByProject(long projectId) {
        return commentRepository.findByProjectId(projectId)
                .stream().map(MapToDTOHelper::mapToCommentDTO)
                .collect(Collectors.toList());
    };
}
