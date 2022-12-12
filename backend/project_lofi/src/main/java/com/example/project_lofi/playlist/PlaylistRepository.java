package com.example.project_lofi.playlist;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT y FROM Playlist y WHERE y.playlistName = ?1")
    public Optional<Playlist> findPlaylistByName(String playlistName);

}
