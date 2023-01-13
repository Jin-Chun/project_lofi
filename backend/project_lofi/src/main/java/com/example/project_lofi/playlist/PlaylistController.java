package com.example.project_lofi.playlist;

import java.rmi.ServerException;
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

    @PostMapping(
        path = "/add",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Playlist> savePlaylist(@RequestBody Playlist playlist) throws ServerException{
        try {
            Playlist savedPlaylist = this.playlistService.savePlaylist(playlist);
            return new ResponseEntity<>(savedPlaylist, HttpStatus.CREATED);
        } catch (Exception e){
            log.error("While saving a given playlist("+playlist.getPlaylistName()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(
        path = "/update",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Playlist> updatePlaylist(@RequestBody Playlist playlist) throws ServerException{
        try {
            Playlist updatedPlaylist = this.playlistService.updatePlaylist(playlist);
            return new ResponseEntity<>(updatedPlaylist, HttpStatus.OK);
        } catch (Exception e){
            log.error("While updating a given playlist("+playlist.getPlaylistId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(
        path = "/update/for/{userId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Playlist> updatePlaylistForUser(@RequestBody Playlist playlist, @PathVariable long userId) throws ServerException{
        try {
            Playlist updatedPlaylist = this.playlistService.updatePlaylist(playlist);
            return new ResponseEntity<>(updatedPlaylist, HttpStatus.OK);
        } catch (Exception e){
            log.error("While updating a given playlist("+playlist.getPlaylistId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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
