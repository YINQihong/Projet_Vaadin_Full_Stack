package com.covoiturage.repository;

import com.covoiturage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 根据email查找用户
    Optional<User> findByEmail(String email);
    
    // 检查email是否存在
    boolean existsByEmail(String email);
}