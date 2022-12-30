package com.example.project_lofi.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_lofi.AbstractService;
import com.example.project_lofi.constants.PL_PlaylistStatus;
import com.example.project_lofi.playlist.Playlist;
import com.example.project_lofi.playlist.PlaylistService;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class UserService extends AbstractService {
    
    private UserRepository userRepository;
    private PlaylistService playlistService;

    @Autowired
    public UserService (UserRepository userRepository, PlaylistService playlistService){
        this.userRepository = userRepository;
        this.playlistService = playlistService;
    }

    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }

    public User getUserById(long userId){
        checkId(userId, "userId");

        Optional<User> existingUser = this.userRepository.findById(userId);
        if (existingUser.isPresent()){
            return existingUser.get();
        } else {
            String message = String.format("No such a User identifier %d is found", userId);
            log.error(message);
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

    public User getUserByNameAndPassword(String userName, String password){
        Optional<User> optionalUser = this.userRepository.findUserByNameAndPassword(userName, password);
        if (optionalUser.isPresent()){
            return optionalUser.get();
        } else {
            String message = "User name or password is wrong!!";
            log.error(message);
            return null;
        }
    }


    public User saveUser(User user){
        Optional<User> existingUser = this.userRepository.findUserByName(user.getUserName());

        if(!existingUser.isPresent()){
            user.setUserId(null);
            user.setUserCreated(LocalDateTime.now());
            user.setUserUpdated(LocalDateTime.now());
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
            user.setUserUpdated(LocalDateTime.now());
            return this.userRepository.save(user);
        } else {
            String message = String.format("No such a lofi info. Cannot update the userId %d, userName %s", 
                user.getUserId(), user.getUserName());
            log.error(message, user);
            throw new IllegalArgumentException(message);
        }
    }

    public User deleteUserById(long userId){
        checkId(userId, "userId");
        Optional<User> existingUser = this.userRepository.findById(userId);

        if(existingUser.isPresent()){
            User userToBeDeleted = existingUser.get();

            for (Playlist playlist : userToBeDeleted.getPlaylists()){
                playlist.setUser(null);
                playlist.setPlaylistStatus(PL_PlaylistStatus.DEACTIVATED);
                this.playlistService.updatePlaylist(playlist);
            }

            userToBeDeleted.setPlaylists(null);
            this.userRepository.delete(userToBeDeleted);

            return userToBeDeleted;
            
        } else {
            String message = String.format("No such a user info. Cannot delete the user, the userId %d", 
                userId);
            log.error(message, userId);
            throw new IllegalArgumentException(message);
        }
    }

    public User createPlaylistForUser(Playlist playlist, long userId){
        checkNull(playlist, "playlist");
        checkId(userId, "userId");
        
        User aUser = this.getUserById(userId);

        playlist.setPlaylistStatus(PL_PlaylistStatus.ACTIVATED);

        // The id will be automatically generated by system sequentially
        playlist.setPlaylistId(null);

        if (!aUser.getPlaylists().isEmpty()) {
            aUser.getPlaylists()
            .stream()
            .forEach(p -> {
                if(p.getPlaylistName().equals(playlist.getPlaylistName())){
                    String message = String.format("User(%d) cannot have the same name(%s) as the playlist(%d)", userId, p.getPlaylistName(), p.getPlaylistId());
                    log.error(message);
                    throw new IllegalArgumentException(message);
                }
            });
        }

        Playlist createdPlaylist = this.playlistService.savePlaylist(playlist);
        createdPlaylist.setUser(aUser);
        
        aUser.getPlaylists().add(createdPlaylist);

        aUser = this.updateUser(aUser);
        log.info(String.format("A new playlist(playlistId: %d) has been added to the user(userId: %d)", createdPlaylist.getPlaylistId(), userId));
        
        return aUser;
    }

    public User removePlaylistFromUser(long playlistId, long userId){
        checkId(userId, "userId");
        checkId(playlistId, "playlistId");

        User aUser = this.getUserById(userId);

        Playlist retrievedPlaylist = this.playlistService.getPlaylistById(playlistId);

        if (aUser.getPlaylists().remove(retrievedPlaylist)){
            log.info(String.format("A given playlist (playlistId: %d) has been removed from the user(userId: %d)", playlistId, userId));
        } else {
            log.info(String.format("A given playlist (playlistId: %d) has not been added to the user(userId: %d)", playlistId, userId));
        }

        retrievedPlaylist.setUser(null);
        retrievedPlaylist.setPlaylistStatus(PL_PlaylistStatus.DEACTIVATED);
        this.playlistService.updatePlaylist(retrievedPlaylist);
        aUser = this.updateUser(aUser);

        return aUser;
    }
}
