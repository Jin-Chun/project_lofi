package com.example.project_lofi.playlist;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project_lofi.AbstractService;
import com.example.project_lofi.constants.PL_PlaylistStatus;
import com.example.project_lofi.playlistassignment.PlaylistLofiAssignment;
import com.example.project_lofi.user.User;
import com.example.project_lofi.user.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * Defines available service APIs related to playlist data
 * 
 * @author Gwanjin
 */
@Service @Slf4j
public class PlaylistService extends AbstractService{
    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserService userService;

    /**
     * Retrieve all playlists if a user has an admin role.
     * 
     * @return a list of all playlists
     */
    public List<Playlist> getAllPlaylists(){
        return this.playlistRepository.findAll();
    }

    public Playlist getPlaylistById(long playlistId){
        checkId(playlistId, "playlistId");
        Optional<Playlist> existingPlaylist = this.playlistRepository.findById(playlistId);
        if (existingPlaylist.isPresent()){
            return existingPlaylist.get();
        } else {
            String message = String.format("No such a Playlist identifier %d is found", playlistId);
            log.error(message, playlistId);
            throw new IllegalArgumentException(message);
        }
    }

    public List<PlaylistLofiAssignment> getAllPlaylistLofiAssignments(long playlistId) {
        checkId(playlistId, "playlistId");
        Playlist playlist = this.getPlaylistById(playlistId);

        if (playlist.getPlaylistLofies() != null && !playlist.getPlaylistLofies().isEmpty()){
            return playlist.getPlaylistLofies();
        } else {
            log.info("The playlist of playlistId %d doesn't have lofies", playlistId);
            return new ArrayList<>();
        }
    }

    public Playlist getPlaylistByName(String playlistName){
        Optional<Playlist> optionalPlaylist = this.playlistRepository.findPlaylistByName(playlistName);
        if (optionalPlaylist.isPresent()){
            return optionalPlaylist.get();
        } else {
            String message = String.format("No such a playlist name %s is found", playlistName);
            log.error(message, playlistName);
            return null;
        }
    }

    public Playlist savePlaylist(Playlist playlist){
        checkNull(playlist, "playlist");
        playlist.setPlaylistId(null);
        playlist.setPlaylistCreated(LocalDateTime.now());
        playlist.setPlaylistUpdated(LocalDateTime.now());
        return this.playlistRepository.saveAndFlush(playlist);
    }

    @Transactional
    public Playlist updatePlaylist(Playlist playlist){
        Optional<Playlist> existingPlaylist = this.playlistRepository.findById(playlist.getPlaylistId());

        if(existingPlaylist.isPresent()){
            playlist.setPlaylistUpdated(LocalDateTime.now());
            return this.playlistRepository.save(playlist);
        } else {
            String message = String.format("No such a playlist info. Cannot update the Playlist of playlistId %d, playlistName %s", 
            playlist.getPlaylistId(), playlist.getPlaylistName());
            log.error(message, playlist);
            throw new IllegalArgumentException(message);
        }
    }

    @Transactional
    public Playlist updatePlaylistForUser(Playlist playlist, long userId){
        checkId(userId, "userId");
        checkId(playlist.getPlaylistId(), "playlistId");

        Playlist p = null;

        Optional<Playlist> existingPlaylist = this.playlistRepository.findById(playlist.getPlaylistId());
        User existingUser = this.userService.getUserById(userId);

        if(existingPlaylist.isPresent() && existingUser != null){
            
            p = existingPlaylist.get();
            p.setPlaylistGenre(playlist.getPlaylistGenre());
            p.setPlaylistName(playlist.getPlaylistName());
            p.setPlaylistStatus(playlist.getPlaylistStatus());
            p.setPlaylistUpdated(LocalDateTime.now());
            
        } else {
            String message = String.format("No such a playlist info. Cannot update the Playlist of playlistId %d, playlistName %s", 
            playlist.getPlaylistId(), playlist.getPlaylistName());
            log.error(message, playlist);
            throw new IllegalArgumentException(message);
        }
        return p;
    }

    public Playlist deletePlaylistById(long playlistId){
        Optional<Playlist> existingPlaylist = this.playlistRepository.findById(playlistId);

        if(existingPlaylist.isPresent()){
            Playlist playlistToBeDeleted = existingPlaylist.get();
            this.playlistRepository.delete(existingPlaylist.get());
            return playlistToBeDeleted;
        } else {
            String message = String.format("No such a playlist info. Cannot delete the playlist, the given playlist identifier: %d", 
                playlistId);
            log.error(message, playlistId);
            throw new IllegalArgumentException(message);
        }
    }

    @Transactional
    public Playlist releasePlaylist(long playlistId){
        checkId(playlistId, "playlistId");

        Playlist retrievedPlaylist = this.getPlaylistById(playlistId);
        checkNull(retrievedPlaylist, "retrievedPlaylist");

        retrievedPlaylist.setPlaylistStatus(PL_PlaylistStatus.RELEASED);
        return this.updatePlaylist(retrievedPlaylist);
    }

    public List<Playlist> getReleasedPlaylists(){
        List<Playlist> releasedPlaylists = this.playlistRepository.findPlaylistsByStatus(PL_PlaylistStatus.RELEASED);

        if(releasedPlaylists == null || releasedPlaylists.isEmpty()){
            log.info("There are no released playlists");
        }
        
        return releasedPlaylists;
    }

    
}
