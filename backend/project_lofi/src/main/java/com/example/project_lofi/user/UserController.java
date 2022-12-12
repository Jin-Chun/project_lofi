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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
            return new ResponseEntity<>(retrievedUserList, HttpStatus.FOUND);
        }
    }

    @GetMapping(path = "/id/{userId}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable long userId){
        User retrievedUser = this.userService.getUserById(userId);
        if(retrievedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedUser, HttpStatus.FOUND);
        }
    }

    @GetMapping(path = "/name/{userName}")
    @ResponseBody
    public ResponseEntity<User> getUserByName(@PathVariable String userName){
        User retrievedUser = this.userService.getUserByName(userName);
        if(retrievedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedUser, HttpStatus.FOUND);
        }
    }

    @PostMapping(
        path = "/add",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> saveUser(@RequestBody User user) throws ServerException{
        User savedUser = this.userService.saveUser(user);
        if (savedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        }
    }

    @PostMapping(
        path = "/update",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> updateUser(@RequestBody User user) throws ServerException{
        User updatedUser = this.userService.updateUser(user);
        if (updatedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);
        }
    }

    @PostMapping(
        path = "/delete",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> deleteUser(@RequestBody User user){
        User deletedUser = this.userService.deleteUserById(user.getUserId());
        if(deletedUser == null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }
}
