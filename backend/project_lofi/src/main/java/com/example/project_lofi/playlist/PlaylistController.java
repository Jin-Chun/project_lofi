package com.example.project_lofi.playlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project_lofi.lofi.Lofi;
import com.example.project_lofi.playlistassignment.PlaylistAssignmentService;
import com.example.project_lofi.playlistassignment.PlaylistLofiAssignment;

import lombok.extern.slf4j.Slf4j;

/**
 * Defines available APIs for processes related playlist data
 * 
 * @author Gwanjin
 */
@RestController @Slf4j
@RequestMapping(path = "/api/playlist")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final PlaylistAssignmentService playlistAssignmentService;

    @Autowired
    public PlaylistController(PlaylistService playlistService, PlaylistAssignmentService playlistAssignmentService){
        this.playlistService = playlistService;
        this.playlistAssignmentService = playlistAssignmentService;
    }

    /**
     * Retrieve all playlists
     * 
     * @return a http response with a list of all playlists or {@link HttpStatus#NOT_FOUND} if no playlists are found
     */
    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<Playlist>> getAllPlaylists(){
        List<Playlist> retrievedPlaylistList = this.playlistService.getAllPlaylists();
        if(retrievedPlaylistList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedPlaylistList, HttpStatus.OK);
        }
    }

    /**
     * Retrieve a list of playlist assignments for a given playlist Id
     * 
     * @param playlistId a given playlist Id
     * @return a http response with a list of playlist assignments or {@link HttpStatus#NOT_FOUND} if no playlist assignments are found
     */
    @GetMapping(path = "/id/{playlistId}/assignments")
    @ResponseBody
    public ResponseEntity<List<PlaylistLofiAssignment>> getAllPlaylistLofiAssignments(@PathVariable long playlistId){
        List<PlaylistLofiAssignment> retrievedPlaylistLofiAssignments = this.playlistService.getAllPlaylistLofiAssignments(playlistId);
        if(retrievedPlaylistLofiAssignments.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedPlaylistLofiAssignments, HttpStatus.OK);
        }
    }

    /**
     * Retrieve a playlist by a given playlist Id
     * 
     * @param playlistId a given playlist id
     * @return a http response with a matched playlist or {@link HttpStatus#NOT_FOUND} if no matched playlists are found
     */
    @GetMapping(path = "/id/{playlistId}")
    @ResponseBody
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable long playlistId){
        Playlist retrievedPlaylist = this.playlistService.getPlaylistById(playlistId);
        if(retrievedPlaylist == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedPlaylist, HttpStatus.OK);
        }
    }

    /**
     * Retrieve a playlist has the same name as a given playlist name
     * 
     * @param playlistName a given playlist name
     * @return a http response with a matched playlist or {@link HttpStatus#NOT_FOUND} if no matched playlists are found
     */
    @GetMapping(path = "/name/{playlistName}")
    @ResponseBody
    public ResponseEntity<Playlist> getPlaylistByName(@PathVariable String playlistName){
        Playlist retrievedPlaylist = this.playlistService.getPlaylistByName(playlistName);
        if(retrievedPlaylist == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedPlaylist, HttpStatus.OK);
        }
    }

    /**
     * Retrieve a list of playlists that have assigned to a given user id
     * 
     * @param userId a given user id
     * @return a http response with a list of all playlists or {@link HttpStatus#NOT_FOUND} if no playlists are found
     */
    @GetMapping(path = "/userid/{userId}")
    @ResponseBody
    public ResponseEntity<List<Playlist>> getAllPlaylistsByUserId(@PathVariable long userId){
        List<Playlist> playlists = this.playlistAssignmentService.getAllPlaylistsByUserId(userId);
        if (playlists != null && !playlists.isEmpty()){
            return new ResponseEntity<>(playlists, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieve a list of playlists which status is released
     * 
     * @return a http response with a list of all releated playlists or {@link HttpStatus#NOT_FOUND} if no releated playlists are found
     */
    @GetMapping(path = "/released")
    @ResponseBody
    public ResponseEntity<List<Playlist>> getReleasedPlaylist(){
        List<Playlist> releasedPlaylists = this.playlistService.getReleasedPlaylists();

        if (releasedPlaylists != null && !releasedPlaylists.isEmpty()){
            return new ResponseEntity<>(releasedPlaylists, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieve a list of playlist lofi assignments by a given playlist id
     * 
     * @param playlistId a given playlist id
     * @return a http response with a list of all playlist lofi assignment or {@link HttpStatus#NOT_FOUND} if no playlist lofi assignments are found
     */
    @GetMapping(path = "/assignment/{playlistId}")
    @ResponseBody
    public ResponseEntity<List<PlaylistLofiAssignment>> getAllPlaylistLofiAssignmentByPlaylistId(@PathVariable long playlistId){
        List<PlaylistLofiAssignment> assignments = this.playlistAssignmentService.getAllPlaylistLofiAssignmentByPlaylistId(playlistId);
        if (assignments != null && !assignments.isEmpty()){
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Save a given playlist data
     * 
     * @param playlist a given playlist
     * @return a saved playlist data or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while saving the playlist data
     */
    @PostMapping(
        path = "/add",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Playlist> savePlaylist(@RequestBody Playlist playlist){
        try {
            Playlist savedPlaylist = this.playlistService.savePlaylist(playlist);
            return new ResponseEntity<>(savedPlaylist, HttpStatus.CREATED);
        } catch (Exception e){
            log.error("While saving a given playlist("+playlist.getPlaylistName()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Update a playlist with a given playlist data
     * 
     * @param playlist a given playlist data
     * @return an updated playlist data or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while updating the playlist data
     */
    @PostMapping(
        path = "/update",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Playlist> updatePlaylist(@RequestBody Playlist playlist){
        try {
            Playlist updatedPlaylist = this.playlistService.updatePlaylist(playlist);
            return new ResponseEntity<>(updatedPlaylist, HttpStatus.OK);
        } catch (Exception e){
            log.error("While updating a given playlist("+playlist.getPlaylistId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Update a playlist with a given playlist data
     * 
     * @param playlist a given playlist data
     * @param userId a given user id
     * @return an updated playlist data or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while updating the playlist data
     */
    @PostMapping(
        path = "/update/for/{userId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Playlist> updatePlaylistForUser(@RequestBody Playlist playlist, @PathVariable long userId){
        try {
            Playlist updatedPlaylist = this.playlistService.updatePlaylist(playlist);
            return new ResponseEntity<>(updatedPlaylist, HttpStatus.OK);
        } catch (Exception e){
            log.error("While updating a given playlist("+playlist.getPlaylistId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Delete a playlist matches to a given playlist data
     * 
     * @param playlist a given playlist
     * @return a deleted playlist data or {@link HttpStatus#INTERNAL_SERVER_ERROR} if error occurs while deleting the playlist data
     */
    @PostMapping(
        path = "/delete",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Playlist> deletePlaylist(@RequestBody Playlist playlist){
        try {
            this.playlistService.deletePlaylistById(playlist.getPlaylistId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            log.error("While deleting a given playlist("+playlist.getPlaylistId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Assign a lofi to a playlist by a given lofi id and playlist id
     * 
     * @param lofiId a given lofi id
     * @param playlistId a given playlist id
     * @return a created playlist lofi assignment data or {@link HttpStatus#INTERNAL_SERVER_ERROR} if error occurs while assigning the lofi to the playlist
     */
    @PostMapping(
        path = "/assign/{lofiId}/to/{playlistId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PlaylistLofiAssignment> assignLofiToPlaylist(@PathVariable long lofiId, @PathVariable long playlistId){
        try {
            PlaylistLofiAssignment playlistLofiAssignment = this.playlistAssignmentService.assignLofiToPlaylist(lofiId, playlistId);
            return new ResponseEntity<>(playlistLofiAssignment, HttpStatus.OK);
        } catch (Exception e){
            log.error("While assigning a lofi("+lofiId+") to the playlist("+playlistId+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Remove a lofi from a playlist by using a given lofi data and playlist id
     * 
     * @param lofi a given lofi data
     * @param playlistId a given playlist id
     * @return an updated playlist data or {@link HttpStatus#INTERNAL_SERVER_ERROR} if error occurs while removing the lofi from the playlist
     */
    @PostMapping(
        path = "/remove/lofi/from/{playlistId}",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
        )
    @ResponseBody
    public ResponseEntity<Playlist> removeLofiFromPlaylist(@RequestBody Lofi lofi, @PathVariable long playlistId){
        try {
            this.playlistAssignmentService.removeLofiFromPlaylist(lofi, playlistId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            log.error("While removing a lofi("+lofi.getLofiId()+") from a playlist("+playlistId+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Remove a lofi from a playlist by using a given playlist lofi assignment data
     * 
     * @param assignment a given playlist lofi assignment
     * @return an updated playlist data or {@link HttpStatus#INTERNAL_SERVER_ERROR} if error occurs while removing the lofi from the playlist
     */
    @PostMapping(
        path = "/remove/lofi",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<Playlist> removeLofiFromPlaylist(@RequestBody PlaylistLofiAssignment assignment){
        try {
            this.playlistAssignmentService.removeLofiFromPlaylist(assignment);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            log.error("While removing a lofi("+assignment.getLofi().getLofiId()+") from a playlist("+assignment.getPlaylist().getPlaylistId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Release a playlist by a given playlist id
     * 
     * @param playlistId a given playlist id
     * @return a released playlist or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while releasing the playlist
     */
    @PostMapping(
        path = "/release/{playlistId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<Playlist> releasePlaylist(@PathVariable long playlistId){
        try {
            Playlist playlist = this.playlistService.releasePlaylist(playlistId);
            return new ResponseEntity<>(playlist, HttpStatus.OK);
        } catch(Exception e){
            log.error("While releasing a given playlist("+playlistId+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Pull a given number of lofies from a pool and assign the lofies to a playlist
     * 
     * @param numOfLofies a given number of lofies to pull
     * @param lofiPoolId a given pool id
     * @param playlistId a given playlist id
     * @return a list of created playlist lofi assignment or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while pulling the lofies
     */
    @PostMapping(
        path = "/pull/{numOfLofies}/from/{lofiPoolId}/for/{playlistId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<List<PlaylistLofiAssignment>> pullLofies(@PathVariable int numOfLofies, @PathVariable long lofiPoolId, @PathVariable long playlistId){
        try {
            List<PlaylistLofiAssignment> assignments = this.playlistAssignmentService.pullLofiesFromLofiPool(lofiPoolId, numOfLofies, playlistId);
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (Exception e){
            String message = String.format("While pulling the number(# %d) of loifes from the lofipool(%d) for the playlist (%d), unexpected error occurs", numOfLofies, lofiPoolId, playlistId);
            log.error(message, e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
