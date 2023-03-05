package com.example.project_lofi.playlist;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Defines JPA queries for additional processes related playlist data
 * 
 * @author Gwanjin
 */
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    /**
     * Retrieve a playlist by using a given playlist name
     * 
     * @param playlistName a given playlist name
     * @return an {@link Optional} playlist that matches to a given playlist name
     */
    @Query("SELECT y FROM Playlist y WHERE y.playlistName = ?1")
    public Optional<Playlist> findPlaylistByName(String playlistName);

    /**
     * Retrieve all playlists by using a given playlist status
     * 
     * @param playlistStatus a given playlist status
     * @return a list of playlists which status matches to a given playlist status
     */
    @Query("SELECT y FROM Playlist y WHERE y.playlistStatus = ?1")
    public List<Playlist> findPlaylistsByStatus(String playlistStatus);

}
