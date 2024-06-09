package com.tvz.hr.craftify.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000, nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;


    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> childComments;

    private LocalDateTime commentTime;

    public Comment(Long id, String comment, Users user, Project project, LocalDateTime commentTime){
        this.id = id;
        this.comment = comment;
        this.user = user;
        this.project = project;
        this.commentTime = commentTime;
    }
    public Comment(Long id, String comment, Users user, Project project, Comment parentComment, LocalDateTime commentTime){
        this.id = id;
        this.comment = comment;
        this.user = user;
        this.project = project;
        this.parentComment = parentComment;
        this.commentTime = commentTime;
    }
}
