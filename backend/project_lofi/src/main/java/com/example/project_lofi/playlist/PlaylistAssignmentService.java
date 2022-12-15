package com.example.project_lofi.playlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_lofi.AbstractService;
import com.example.project_lofi.lofi.Lofi;
import com.example.project_lofi.lofi.LofiService;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class PlaylistAssignmentService extends AbstractService{
    
    private final PlaylistService playlistService;
    private final LofiService lofiService;

    @Autowired
    public PlaylistAssignmentService(PlaylistService playlistService, LofiService lofiService){
        this.playlistService = playlistService;
        this.lofiService = lofiService;
    }

    public Playlist assignLofiToPlaylist(long lofiId, long playlistId){
        
        // #1. verify a given lofi id and playlist id exist
        Lofi lofi = this.lofiService.getLofiById(lofiId);
        Playlist playlist = this.playlistService.getPlaylistById(playlistId);

        checkNull(lofi, "lofi");
        checkNull(playlist, "playlist");

        // #2. add the lofi to the playlistLofies
        playlist.getPlaylistLofies().add(lofi);

        // #3. save the playlist
        Playlist updatedPlaylist = this.playlistService.updatePlaylist(playlist);
        log.info(String.format("A given lofi (lofiId: %d) has been assigned to the playlist(playlistId: %d)", lofiId, playlistId));
        // #4. return the playlist
        return updatedPlaylist;
    }

    public Playlist assignLofiesToPlaylist(List<Long> lofiIds, long playlistId){
        checkNullAndEmpty(lofiIds, "lofiIds");
        checkId(playlistId, "playlistId");

        Playlist playlist = this.playlistService.getPlaylistById(playlistId);
        checkNull(playlist, "playlist");

        for (Long lofiId : lofiIds){

            try {
                Lofi lofi = this.lofiService.getLofiById(lofiId);
                playlist.getPlaylistLofies().add(lofi);
                log.info(String.format("A given lofi (lofiId: %d) has been assigned to the playlist(playlistId: %d)", lofiId, playlistId));
            } catch (Exception e){
                log.error(String.format("While retrieving a lofi (lofiId: %d), unexpected error occurs", lofiId));
            }
        }

        Playlist updatedPlaylist = this.playlistService.updatePlaylist(playlist);
        return updatedPlaylist;
    }

    public Playlist removeLofiFromPlaylist(long lofiId, long playlistId){
        // #1. verify a given lofi id and playlist id exist
        Lofi lofi = this.lofiService.getLofiById(lofiId);
        Playlist playlist = this.playlistService.getPlaylistById(playlistId);

        checkNull(lofi, "lofi");
        checkNull(playlist, "playlist");

        // #2. remove the lofi from the playlistLofies
        if (playlist.getPlaylistLofies().remove(lofi)){
            log.info(String.format("A given lofi (lofiId: %d) has been removed from the playlist(playlistId: %d)", lofiId, playlistId));
        } else {
            log.info(String.format("A given lofi (lofiId: %d) has not been assigned to the playlist(playlistId: %d)", lofiId, playlistId));
        }

        // #3. save the playlist
        Playlist updatedPlaylist = this.playlistService.updatePlaylist(playlist);

        // #4. return the playlist
        return updatedPlaylist;
    }

}
