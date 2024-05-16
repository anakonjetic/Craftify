package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.service.CommentService;
import com.tvz.hr.craftify.service.dto.CommentDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable long id) {
        Optional<CommentDTO> commentOptional = commentService.getCommentById(id);
        return commentOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byProject/{projectId}")
    public List<Comment> getCommentsByProject(@PathVariable long projectId) {
        return commentService.getCommentsByProject(projectId);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        commentService.createComment(comment);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable long id, @RequestBody Comment comment) {
        try{
            commentService.updateComment(comment, id);
            return new ResponseEntity<>(comment, HttpStatus.OK);
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
