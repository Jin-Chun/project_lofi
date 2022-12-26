package com.example.project_lofi.playlistassignment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_lofi.AbstractService;
import com.example.project_lofi.lofi.Lofi;
import com.example.project_lofi.lofi.LofiService;
import com.example.project_lofi.playlist.Playlist;
import com.example.project_lofi.playlist.PlaylistService;
import com.example.project_lofi.user.User;
import com.example.project_lofi.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class PlaylistAssignmentService extends AbstractService{

    @Autowired
    private PlaylistLofiAssignmentRepository repository;

    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private LofiService lofiService;
    @Autowired
    private UserService userService;

    public List<Playlist> getAllPlaylistsByUserId(long userId){
        try {
            User user = this.userService.getUserById(userId);
            return user.getPlaylists();
        } catch (Exception e){
            String message = String.format("Cannot retrieve playlist info from the given userId(%d) which doesn't exist", userId);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    public PlaylistLofiAssignment savePlaylistLofiAssignment(PlaylistLofiAssignment assignment){
        return repository.save(assignment);
    }

    public PlaylistLofiAssignment deletePlaylistLofiAssignment(PlaylistLofiAssignment assignment){
        repository.delete(assignment);
        return assignment;
    }

    public List<PlaylistLofiAssignment> getAllPlaylistLofiAssignmentByPlaylistId(long playlistId){
        return repository.findAllByPlaylistId(playlistId);
    }

    public PlaylistLofiAssignment assignLofiToPlaylist(long lofiId, long playlistId){
        
        // #1. verify a given lofi id and playlist id exist
        Lofi lofi = this.lofiService.getLofiById(lofiId);
        Playlist playlist = this.playlistService.getPlaylistById(playlistId);

        checkNull(lofi, "lofi");
        checkNull(playlist, "playlist");

        // #2. create a new playlist lofi assignment
        PlaylistLofiAssignment assignment = this.createNewPlaylistLofiAssignment(playlist, lofi);
        
        // #3. add the lofi to the playlistLofies and lofi's playlists
        playlist.getPlaylistLofies().add(assignment);
        lofi.getPlaylists().add(assignment);

        // #4. update the playlist and lofi
        this.playlistService.updatePlaylist(playlist);
        this.lofiService.updateLofi(lofi);
        log.info(String.format("A given lofi (lofiId: %d) has been assigned to the playlist(playlistId: %d)", lofiId, playlistId));
        
        // #4. return the playlist
        return assignment;
    }

    public List<PlaylistLofiAssignment> assignLofiesToPlaylist(List<Long> lofiIds, long playlistId){
        checkNullAndEmpty(lofiIds, "lofiIds");
        checkId(playlistId, "playlistId");

        Playlist playlist = this.playlistService.getPlaylistById(playlistId);
        checkNull(playlist, "playlist");

        List<PlaylistLofiAssignment> result = new ArrayList<>();
        for (Long lofiId : lofiIds){

            try {
                result.add(this.assignLofiToPlaylist(lofiId, playlistId));
            } catch (Exception e){
                log.error(String.format("While assigning a lofi (lofiId: %d) to a playlist (playlistId: %d), unexpected error occurs", lofiId, playlistId));
            }
        }

        return result;
    }

    public void removeLofiFromPlaylist(Lofi lofi, long playlistId){
        checkNull(lofi, "lofi");
        checkId(playlistId, "playlistId");
        long lofiId = lofi.getLofiId();
        checkId(lofiId, "lofiId");

        // #1. verify a given lofi id and playlist id exist
        Lofi existingLofi = this.lofiService.getLofiById(lofiId);
        Playlist playlist = this.playlistService.getPlaylistById(playlistId);
        
        checkNull(existingLofi, "lofi");
        checkNull(playlist, "playlist");

        PlaylistLofiAssignment assignment = null;

        List<PlaylistLofiAssignment> assignments = this.getAllPlaylistLofiAssignmentByPlaylistId(playlistId);

        if (assignments != null && !assignments.isEmpty()){
            for (PlaylistLofiAssignment assign : assignments){
                if(assign.getLofi().getLofiId().equals(existingLofi.getLofiId())){
                    assignment = assign;
                }
            }
        }

        if (assignment == null) {
            String message = String.format("The lofi (lofiId: %d) has not been assigned to the playlist (playlistId: %d)", lofiId, playlistId);
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        // #2. remove the lofi from the playlistLofies
        if (playlist.getPlaylistLofies().remove(assignment)){

            if (lofi.getPlaylists() != null){
                lofi.getPlaylists().remove(assignment);
            }
            log.info(String.format("A given lofi (lofiId: %d) has been removed from the playlist(playlistId: %d)", lofi.getLofiId(), playlist.getPlaylistId()));
        } else {
            log.info(String.format("A given lofi (lofiId: %d) has not been assigned to the playlist(playlistId: %d)", lofi.getLofiId(), playlist.getPlaylistId()));
        }

        // #3. save the playlist
        this.playlistService.updatePlaylist(playlist);
        this.lofiService.updateLofi(lofi);

        // #4. delete the playlist-lofi assignment
        this.deletePlaylistLofiAssignment(assignment);
    }

    private PlaylistLofiAssignment createNewPlaylistLofiAssignment(Playlist playlist, Lofi lofi){
        PlaylistLofiAssignment assignment = new PlaylistLofiAssignment();
        PlaylistLofiAssignmentKey key = new PlaylistLofiAssignmentKey();
        key.setLofiId(lofi.getLofiId());
        key.setPlaylistId(playlist.getPlaylistId());
        assignment.setPlaylistLofiAssignmentId(key);
        assignment.setLofi(lofi);
        assignment.setPlaylist(playlist);
        assignment.setPlaylistLofiAssignmentTime(LocalDateTime.now());
        this.savePlaylistLofiAssignment(assignment);
        
        return assignment;
    }
}
