package com.lrbell.llamabot.repository;

import com.lrbell.llamabot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String userName);
    Optional<User> findByEmail(String email);
}
