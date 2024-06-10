package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> getFirstByUsernameOrEmailIgnoreCase(String username, String email);
    Users findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM project_subscribers WHERE project_id IN (SELECT id FROM project WHERE user_id = :id)", nativeQuery = true)
    void deleteProjectSubscribersByUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_project_likes WHERE project_id IN (SELECT id FROM project WHERE user_id = :id)", nativeQuery = true)
    void deleteUserProjectLikesByProjectUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM favorites WHERE project_id IN (SELECT id FROM project WHERE user_id = :id)", nativeQuery = true)
    void deleteFavoritesByProjectUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM comment WHERE parent_comment_id IN (SELECT id FROM comment WHERE project_id IN (SELECT id FROM project WHERE user_id = :id))", nativeQuery = true)
    void deleteChildCommentsByProjectUserId(@Param("id") Long id);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM comment WHERE project_id IN (SELECT id FROM project WHERE user_id = :id)", nativeQuery = true)
    void deleteCommentsByProjectUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM project_subscribers WHERE user_id = :id", nativeQuery = true)
    void deleteProjectSubscribersByUserIdOnly(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_project_likes WHERE user_id = :id", nativeQuery = true)
    void deleteUserProjectLikesByUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_subscribers WHERE user_id = :id OR followed_user_id = :id", nativeQuery = true)
    void deleteUserSubscribersByUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM media WHERE project_id IN (SELECT id from project WHERE user_id = :id)", nativeQuery = true)
    void deleteMediaProjectsByUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM project WHERE user_id = :id", nativeQuery = true)
    void deleteProjectsByUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_preferences WHERE user_id = :id", nativeQuery = true)
    void deleteUserPreferencesByUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM favorites WHERE user_id = :id", nativeQuery = true)
    void deleteFavoritesByUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM refresh_token WHERE user_id = :id", nativeQuery = true)
    void deleteRefreshTokensByUserId(@Param("id") Long id);
}
