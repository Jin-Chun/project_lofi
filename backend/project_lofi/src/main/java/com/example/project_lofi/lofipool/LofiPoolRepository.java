package com.example.project_lofi.lofipool;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LofiPoolRepository extends JpaRepository<LofiPool, Long> {
    
    @Query("SELECT p FROM LofiPool p WHERE p.lofiPoolName = ?1")
    public Optional<LofiPool> findLofiPoolByName(String lofiPoolName);
}
