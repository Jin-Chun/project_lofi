package com.example.project_lofi.lofi;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Defines JPA queries for additional process of Lofi data
 * 
 * @author Gwanjin
 */
@Repository
public interface LofiRepository extends JpaRepository<Lofi, Long> {
 
    /**
     * Retrieves a lofi by a given lofi name
     * 
     * @param lofiName a given lofi name
     * @return a {@link Optional} lofi which name is a match to the given lofi name
     */
    @Query("SELECT l FROM Lofi l WHERE l.lofiName = ?1")
    Optional<Lofi> findLofiByName(String lofiName);

    /**
     * Retrieves matched lofies which types are a given lofi type
     * 
     * @param lofiType a given lofi type
     * @return a list of matched lofies or empty list
     */
    @Query("SELECT l FROM Lofi l WHERE l.lofiType = ?1")
    List<Lofi> findLofiesByType(String lofiType);

    /**
     * Retrieves matched lofies that names contain a given keyword
     * 
     * @param keyword a given keyword
     * @return a list of matched lofies or emtpy list
     */
    @Query("SELECT l FROM Lofi l WHERE l.lofiName like %?1%")
    List<Lofi> findLofiesByKeyword(String keyword);
}
