package com.tvz.hr.craftify.repository;

import com.tvz.hr.craftify.model.UserSubscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscribersRepository extends JpaRepository<UserSubscribers, Long> {
}
