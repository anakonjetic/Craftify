package com.tvz.hr.craftify.repository;
import com.tvz.hr.craftify.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProjectId(Long projectId);
    Optional<Comment> findByUserId(Long id);
}
