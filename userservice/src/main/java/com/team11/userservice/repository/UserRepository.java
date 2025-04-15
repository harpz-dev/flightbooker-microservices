package com.team11.userservice.repository;

import com.team11.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}