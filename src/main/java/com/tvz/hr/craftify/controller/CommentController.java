package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.service.CommentService;
import com.tvz.hr.craftify.service.dto.CommentDTO;
import com.tvz.hr.craftify.service.dto.CommentPostPutDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private CommentService commentService;

    @GetMapping("/all")
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments();
    }
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable long id) {
        Optional<CommentDTO> commentOptional = commentService.getCommentById(id);
        return commentOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byProject/{projectId}")
    public List<CommentDTO> getCommentsByProject(@PathVariable long projectId) {
        return commentService.getCommentsByProject(projectId);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentPostPutDTO comment) {
        return new ResponseEntity<>(
                commentService.createComment(comment),
                HttpStatus.CREATED
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable long id, @RequestBody CommentPostPutDTO comment) {
        try{
            return new ResponseEntity<>(
                    commentService.updateComment(comment, id),
                    HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

}
