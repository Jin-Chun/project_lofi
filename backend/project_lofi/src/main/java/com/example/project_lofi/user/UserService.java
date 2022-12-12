package com.example.project_lofi.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class UserService {
    
    private UserRepository userRepository;

    @Autowired
    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }

    public User getUserById(long userId){
        Optional<User> existingUser = this.userRepository.findById(userId);
        if (existingUser.isPresent()){
            return existingUser.get();
        } else {
            String message = String.format("No such a User identifier %d is found", userId);
            log.error(message, userId);
            throw new IllegalArgumentException(message);
        }
    }

    public User getUserByName(String userName){
        Optional<User> optionalUser = this.userRepository.findUserByName(userName);
        if (optionalUser.isPresent()){
            return optionalUser.get();
        } else {
            String message = String.format("No such a user name %s is found", userName);
            log.error(message, userName);
            return null;
        }
    }

    public User saveUser(User user){
        Optional<User> existingUser = this.userRepository.findUserByName(user.getUserName());

        if(!existingUser.isPresent()){
            return this.userRepository.save(user);
        } else {   
            String message = String.format("Cannot save the same User name %s", user.getUserName());
            log.error(message, user);
            throw new IllegalArgumentException(message);
        }
    }

    public User updateUser(User user){
        Optional<User> existingUser = this.userRepository.findById(user.getUserId());

        if(existingUser.isPresent()){
            return this.userRepository.save(user);
        } else {
            String message = String.format("No such a lofi info. Cannot update the userId %d, userName %s", 
                user.getUserId(), user.getUserName());
            log.error(message, user);
            throw new IllegalArgumentException(message);
        }
    }

    public User deleteUserById(long userId){
        Optional<User> existingUser = this.userRepository.findById(userId);

        if(existingUser.isPresent()){
            User userToBeDeleted = existingUser.get();
            this.userRepository.delete(existingUser.get());
            return userToBeDeleted;
        } else {
            String message = String.format("No such a user info. Cannot delete the user, the userId %d", 
                userId);
            log.error(message, userId);
            throw new IllegalArgumentException(message);
        }
    }
}
