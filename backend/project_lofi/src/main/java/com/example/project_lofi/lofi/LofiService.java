package com.example.project_lofi.lofi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_lofi.AbstractService;
import com.example.project_lofi.lofipool.LofiPool;
import com.example.project_lofi.lofipool.LofiPoolService;
import com.example.project_lofi.playlist.Playlist;
import com.example.project_lofi.playlist.PlaylistService;
import com.example.project_lofi.playlistassignment.PlaylistAssignmentService;

import lombok.extern.slf4j.Slf4j;

/**
 * Defines logics of the lofi data process 
 * 
 * @author Gwanjin
 */
@Service @Slf4j
public class LofiService extends AbstractService{
    
    @Autowired
    private LofiRepository lofiRepository;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlaylistAssignmentService plAssignmentService;
    @Autowired
    private LofiPoolService lofiPoolService;

    /**
     * Retrieves all lofies
     * 
     * @return a list of all lofies or empty list
     */
    public List<Lofi> getAllLofies(){
        return this.lofiRepository.findAll();
    }

    /**
     * Retrieves a lofi by a given lofi id
     * 
     * @param lofiId a given lofi id
     * @return a matched lofi or {@link IllegalAccessException} if no matched lofi is found
     */
    public Lofi getLofiById(long lofiId){
        checkId(lofiId, "lofiId");

        Optional<Lofi> optionalLofi = this.lofiRepository.findById(lofiId);
        if (optionalLofi.isPresent()){
            return optionalLofi.get();
        } else {
            String message = String.format("No such a lofi identifier %d is found", lofiId);
            log.error(message, lofiId);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Retrieves a lofi by a given lofi name
     * 
     * @param lofiName a given lofi name
     * @return a matched lofi or null if any lofies do not match the given name
     */
    public Lofi getLofiByName(String lofiName){
        checkNull(lofiName, "lofiName");

        Optional<Lofi> optionalLofi = this.lofiRepository.findLofiByName(lofiName);
        if (optionalLofi.isPresent()){
            return optionalLofi.get();
        } else {
            String message = String.format("No such a lofi name %s is found", lofiName);
            log.error(message, lofiName);
            return null;
        }
    }

    /**
     * Retrieves all lofies by a given lofi type
     * 
     * @param lofiType a given lofi type
     * @return a list of matched lofies or empty list
     */
    public List<Lofi> getLofiesByType(String lofiType){
        checkNull(lofiType, "lofiType");

        List<Lofi> retrievedLofi = this.lofiRepository.findLofiesByType(lofiType);

        if (retrievedLofi == null || retrievedLofi.isEmpty()){
            log.info("There are no lofies of the type: "+lofiType);
        }

        return retrievedLofi;
    }

    /**
     * Retrieves all lofies that names match a given keyword
     * 
     * @param keyword a given keyword
     * @return a list of matched lofies or empty list
     */
    public List<Lofi> getLofiesByKeyword(String keyword){
        checkNull(keyword, "keyword");

        List<Lofi> retrievedLofi = this.lofiRepository.findLofiesByKeyword(keyword);

        if (retrievedLofi == null || retrievedLofi.isEmpty()){
            log.info("There are no lofies of the keyword: "+keyword);
        }

        return retrievedLofi;
    }

    /**
     * Retrieves all lofies that have been assigned to a playlist by a given playlist id
     * 
     * @param playlistId a given playlist id
     * @return a list of matched lofies that have been assigned to a playlist or empty list
     */
    public List<Lofi> getAllLofiesAssignedToPlaylist(long playlistId){
        checkId(playlistId, "playlistId");

        Playlist playlist = this.playlistService.getPlaylistById(playlistId);
        List<Lofi> lofies = new ArrayList<>();
        playlist.getPlaylistLofies().forEach(a -> lofies.add(a.getLofi()));
        
        return lofies;
    }

    /**
     * Retrieves all lofies that a user has in one's playlists by a given user id
     * 
     * @param userId a given user id
     * @return a list of matched lofies or empty list
     */
    public List<Lofi> getAllLofiesByUserId(long userId){
        checkId(userId, "userId");

        List<Playlist> playlists = this.plAssignmentService.getAllPlaylistsByUserId(userId);
        List<Lofi> lofies = new ArrayList<>();
        
        if (playlists != null && !playlists.isEmpty()){
            playlists.forEach(p -> p.getPlaylistLofies().forEach(a -> {
                if (!lofies.contains(a.getLofi())){
                    lofies.add(a.getLofi());
                } 
            }));
        }

        return lofies;
    }

    /**
     * Retrieves all lofies that have been assigned to a lofi pool by a given lofi pool id
     * 
     * @param lofiPoolId a given lofi pool id
     * @return a list of matched lofies or empty list
     */
    public List<Lofi> getAllLofiesAssignedToLofiPool(long lofiPoolId){
        checkId(lofiPoolId, "lofiPoolId");

        LofiPool lofiPool = this.lofiPoolService.getLofiPoolById(lofiPoolId);
        return lofiPool.getPoolLofies();
    }

    /**
     * Saves a given lofi data
     * 
     * @param lofi a given lofi data
     * @return a saved lofi data or {@link IllegalArgumentException} if there is a matched lofi with the given lofi data
     */
    public Lofi saveLofi(Lofi lofi){
        // Retrieve a lofi by a given lofi's name
        Optional<Lofi> existingLofi = this.lofiRepository.findLofiByName(lofi.getLofiName());

        // If the existing lofi is not present, save the given lofi. Otherwise, throw IllegalArgumentException
        if(!existingLofi.isPresent()){
            lofi.setLofiId(null);
            return this.lofiRepository.save(lofi);

        } else {   
            String message = String.format("Cannot save the same Lofi name %s", lofi.getLofiName());
            log.error(message, lofi);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Updates a given lofi data
     * 
     * @param lofi a given lofi data
     * @return a updated lofi data or {@link IllegalArgumentException} if there is no lofies with the given lofi data
     */
    public Lofi updateLofi(Lofi lofi){
        // Retrieve a lofi by a given lofi's Id
        Optional<Lofi> existingLofi = this.lofiRepository.findById(lofi.getLofiId());

        // If the existing lofi is present, update the existing lofi with the given lofi. Otherwise, throw IllegalArgumentException
        if(existingLofi.isPresent()){
            return this.lofiRepository.save(lofi);
        } else {
            String message = String.format("No such a lofi info. Cannot update the lofiId %d, lofiName %s", 
                lofi.getLofiId(), lofi.getLofiName());
            log.error(message, lofi);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Deletes a lofi with a given lofi Id
     * 
     * @param lofiId a given lofi id
     * @return a deleted lofi or {@link IllegalArgumentException} if there is no matched lofi with a given lof id
     */
    public Lofi deleteLofiById(long lofiId){
        // Retrieve a lofi by a given lofi id
        Optional<Lofi> existingLofi = this.lofiRepository.findById(lofiId);

        // If the existing lofi is present, delete the lofi. Otherwise, throw IllegalArgumentException
        if(existingLofi.isPresent()){
            Lofi lofiToBeDeleted = existingLofi.get();
            this.lofiRepository.delete(existingLofi.get());
            return lofiToBeDeleted;
        } else {
            String message = String.format("No such a lofi info. Cannot delete the lofi, the lofi Id %d", 
                lofiId);
            log.error(message, lofiId);
            throw new IllegalArgumentException(message);
        }
    }

}
