package com.example.project_lofi.lofipool;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Defines JPA queries for additional process of Lofi pool data
 * 
 * @author Gwanjin
 */
@Repository
public interface LofiPoolRepository extends JpaRepository<LofiPool, Long> {
    
    /**
     * Retrieves a lofi pool by a given lofi pool name
     * 
     * @param lofiPoolName a given lofi pool name
     * @return a {@link Optional} lofi pool which name is a match to the given lofi pool name
     */
    @Query("SELECT p FROM LofiPool p WHERE p.lofiPoolName = ?1")
    public Optional<LofiPool> findLofiPoolByName(String lofiPoolName);
}
