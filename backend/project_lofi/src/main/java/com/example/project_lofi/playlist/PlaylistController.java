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

@RestController
@RequestMapping(path = "/api/playlist")
public class PlaylistController {
    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService){
        this.playlistService = playlistService;
    }

    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<Playlist>> getAllPlaylists(){
        List<Playlist> retrievedPlaylistList = this.playlistService.getAllPlaylists();
        if(retrievedPlaylistList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedPlaylistList, HttpStatus.FOUND);
        }
    }

    @GetMapping(path = "/id/{playlistId}")
    @ResponseBody
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable long playlistId){
        Playlist retrievedPlaylist = this.playlistService.getPlaylistById(playlistId);
        if(retrievedPlaylist == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedPlaylist, HttpStatus.FOUND);
        }
    }

    @GetMapping(path = "/name/{playlistName}")
    @ResponseBody
    public ResponseEntity<Playlist> getPlaylistByName(@PathVariable String playlistName){
        Playlist retrievedPlaylist = this.playlistService.getPlaylistByName(playlistName);
        if(retrievedPlaylist == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedPlaylist, HttpStatus.FOUND);
        }
    }

    @PostMapping(
        path = "/add",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Playlist> savePlaylist(@RequestBody Playlist playlist) throws ServerException{
        Playlist savedPlaylist = this.playlistService.savePlaylist(playlist);
        if (savedPlaylist == null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(savedPlaylist, HttpStatus.CREATED);
        }
    }
    
    @PostMapping(
        path = "/update",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Playlist> updatePlaylist(@RequestBody Playlist playlist) throws ServerException{
        Playlist updatedPlaylist = this.playlistService.updatePlaylist(playlist);
        if (updatedPlaylist == null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(updatedPlaylist, HttpStatus.ACCEPTED);
        }
    }

    @PostMapping(
        path = "/delete",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Playlist> deletePlaylist(@RequestBody Playlist playlist){
        Playlist deletedPlaylist = this.playlistService.deletePlaylistById(playlist.getPlaylistId());
        if(deletedPlaylist == null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }
}
