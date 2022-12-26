package com.example.project_lofi.playlistassignment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistLofiAssignmentRepository extends JpaRepository<PlaylistLofiAssignment, PlaylistLofiAssignmentKey> {

    @Query("SELECT a FROM PlaylistLofiAssignment a WHERE a.playlistLofiAssignmentId.playlistId = ?1")
    public List<PlaylistLofiAssignment> findAllByPlaylistId(long playlistId);
}
