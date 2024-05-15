package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.request.UsersRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    //static final String SELECT_ALL = "SELECT id, username, email, password, is_admin FROM users";
    @Query("SELECT new com.tvz.hr.craftify.request.UsersRequest(id, username, email, password, isAdmin) FROM Users")
    List<UsersRequest> getAllUsers();

    @Query("SELECT new com.tvz.hr.craftify.request.UsersRequest(id, username, email, password, isAdmin) FROM Users WHERE id = ?1")
    Optional<UsersRequest> getUserById(Long id);

    @Query("SELECT new com.tvz.hr.craftify.model.Comment(id, comment, user, project, commentTime) FROM Comment WHERE user.id = ?1")
    List<Comment> getUserComments(Long id);
}
