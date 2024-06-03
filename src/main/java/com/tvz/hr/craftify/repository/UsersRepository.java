package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.Comment;
import com.tvz.hr.craftify.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findFirstByUsernameOrEmailIgnoreCase(String username, String email);
    Optional<Users> getFirstByUsernameOrEmailIgnoreCase(String username, String email);
    Users findByUsername(String username);
}
