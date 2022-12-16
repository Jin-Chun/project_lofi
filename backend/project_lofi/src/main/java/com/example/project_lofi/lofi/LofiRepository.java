package com.example.project_lofi.lofi;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LofiRepository extends JpaRepository<Lofi, Long> {
 
    @Query("SELECT l FROM Lofi l WHERE l.lofiName = ?1")
    Optional<Lofi> findLofiByName(String lofiName);

    @Query("SELECT l FROM Lofi l WHERE l.lofiType = ?1")
    List<Lofi> findLofiesByType(String lofiType);

    @Query("SELECT l FROM Lofi l WHERE l.lofiName like %?1%")
    List<Lofi> findLofiesByKeyword(String keyword);
}
