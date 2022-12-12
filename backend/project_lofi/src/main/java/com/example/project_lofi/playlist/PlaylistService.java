package com.example.project_lofi.playlist;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository){
        this.playlistRepository = playlistRepository;
    }

    public List<Playlist> getAllPlaylists(){
        return this.playlistRepository.findAll();
    }

    public Playlist getPlaylistById(long playlistId){
        Optional<Playlist> existingPlaylist = this.playlistRepository.findById(playlistId);
        if (existingPlaylist.isPresent()){
            return existingPlaylist.get();
        } else {
            String message = String.format("No such a Playlist identifier %d is found", playlistId);
            log.error(message, playlistId);
            throw new IllegalArgumentException(message);
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
        Optional<Playlist> existingPlaylist = this.playlistRepository.findPlaylistByName(playlist.getPlaylistName());

        if(!existingPlaylist.isPresent()){
            return this.playlistRepository.save(playlist);
        } else {   
            String message = String.format("Cannot save the same Playlist name %s", playlist.getPlaylistName());
            log.error(message, playlist);
            throw new IllegalArgumentException(message);
        }
    }

    public Playlist updatePlaylist(Playlist playlist){
        Optional<Playlist> existingPlaylist = this.playlistRepository.findById(playlist.getPlaylistId());

        if(existingPlaylist.isPresent()){
            return this.playlistRepository.save(playlist);
        } else {
            String message = String.format("No such a playlist info. Cannot update the Playlist of playlistId %d, playlistName %s", 
            playlist.getPlaylistId(), playlist.getPlaylistName());
            log.error(message, playlist);
            throw new IllegalArgumentException(message);
        }
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

}
