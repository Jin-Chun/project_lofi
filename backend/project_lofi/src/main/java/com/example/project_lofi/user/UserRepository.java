package com.example.project_lofi.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
    @Query("SELECT u FROM User u WHERE u.userName = ?1")
    Optional<User> findUserByName(String userName);

    @Query("SELECT u FROM User u WHERE u.userName = ?1 AND u.userPassword = ?2")
    Optional<User> findUserByNameAndPassword(String userName, String userPassword);
}
