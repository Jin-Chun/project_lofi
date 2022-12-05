package com.example.project_lofi.lofi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LofiRepository extends JpaRepository<Lofi, Long> {
    
}
