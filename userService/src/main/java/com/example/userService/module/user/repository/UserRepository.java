package com.example.userService.module.user.repository;

import com.example.userService.module.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserId(String userId);
    
    Optional<User> findByUserIdAndUseYn(String id, String useYn);
    
    Optional<User> findByEmailAndUseYn(String email, String useYn);
    
}

