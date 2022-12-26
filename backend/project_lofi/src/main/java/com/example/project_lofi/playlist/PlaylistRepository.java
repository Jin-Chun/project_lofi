package com.example.project_lofi.playlist;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT y FROM Playlist y WHERE y.playlistName = ?1")
    public Optional<Playlist> findPlaylistByName(String playlistName);

    @Query("SELECT y FROM Playlist y WHERE y.playlistStatus = ?1")
    public List<Playlist> findPlaylistsByStatus(String playlistStatus);

}
