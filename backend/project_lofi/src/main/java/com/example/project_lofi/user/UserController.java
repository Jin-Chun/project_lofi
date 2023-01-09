package com.example.project_lofi.user;

import java.rmi.ServerException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project_lofi.playlist.Playlist;

import lombok.extern.slf4j.Slf4j;

@RestController @Slf4j
@RequestMapping(path = "/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> retrievedUserList = this.userService.getAllUsers();
        if(retrievedUserList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedUserList, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/id/{userId}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable long userId){
        User retrievedUser = this.userService.getUserById(userId);
        if(retrievedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedUser, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/name/{userName}")
    @ResponseBody
    public ResponseEntity<User> getUserByName(@PathVariable String userName){
        User retrievedUser = this.userService.getUserByName(userName);
        if(retrievedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedUser, HttpStatus.OK);
        }
    }

    @PostMapping(
        path = "/login/{userName}/{password}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<User> getUserByNameAndPassword(@PathVariable String userName, @PathVariable String password){
        User retrievedUser = this.userService.getUserByNameAndPassword(userName, password);
        if(retrievedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedUser, HttpStatus.OK);
        }  
    }

    @PostMapping(
        path = "/add",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> saveUser(@RequestBody User user) throws ServerException{
        try{
            User savedUser = this.userService.saveUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (Exception e){
            log.error("While saving a user("+user.getUserName()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(
        path = "/update",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> updateUser(@RequestBody User user) throws ServerException{
        try{
            User updatedUser = this.userService.updateUser(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e){
            log.error("While updating a user("+user.getUserId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(
        path = "/delete",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> deleteUser(@RequestBody User user){
        try{
            this.userService.deleteUserById(user.getUserId());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e){
            log.error("While deleting a user("+user.getUserId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(
        path = "create/playlist/{userId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<User> createPlaylistForUser(@RequestBody Playlist playlist, @PathVariable long userId){
        try{
            User updatedUser = this.userService.createPlaylistForUser(playlist, userId);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e){
            log.error("While creating a playlist("+playlist.getPlaylistName()+") for a user("+userId+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(
        path = "remove/playlist/{playlistId}/from/{userId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<User> removePlaylistFromUser(@PathVariable long playlistId, @PathVariable long userId){
        try{
            User updatedUser = this.userService.removePlaylistFromUser(playlistId, userId);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e){
            log.error("While removing a playlist("+playlistId+") from a user("+userId+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
