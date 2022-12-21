package com.example.project_lofi.lofi;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_lofi.AbstractService;
import com.example.project_lofi.lofipool.LofiPool;
import com.example.project_lofi.lofipool.LofiPoolService;
import com.example.project_lofi.playlist.Playlist;
import com.example.project_lofi.playlist.PlaylistService;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class LofiService extends AbstractService{
    
    private final LofiRepository lofiRepository;
    private final PlaylistService playlistService;
    private final LofiPoolService lofiPoolService;

    @Autowired
    public LofiService(LofiRepository lofiRepository, PlaylistService playlistService, LofiPoolService lofiPoolService){
        this.lofiRepository = lofiRepository;
        this.playlistService = playlistService;
        this.lofiPoolService = lofiPoolService;
    }

    public List<Lofi> getAllLofies(){
        return this.lofiRepository.findAll();
    }

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

    public List<Lofi> getLofiesByType(String lofiType){
        checkNull(lofiType, "lofiType");

        List<Lofi> retrievedLofi = this.lofiRepository.findLofiesByType(lofiType);

        if (retrievedLofi == null || retrievedLofi.isEmpty()){
            log.info("There are no lofies of the type: "+lofiType);
        }

        return retrievedLofi;
    }

    public List<Lofi> getLofiesByKeyword(String keyword){
        checkNull(keyword, "keyword");

        List<Lofi> retrievedLofi = this.lofiRepository.findLofiesByKeyword(keyword);

        if (retrievedLofi == null || retrievedLofi.isEmpty()){
            log.info("There are no lofies of the keyword: "+keyword);
        }

        return retrievedLofi;
    }

    public List<Lofi> getAllLofiesAssignedToPlaylist(long playlistId){
        checkId(playlistId, "playlistId");

        Playlist playlist = this.playlistService.getPlaylistById(playlistId);
        return playlist.getPlaylistLofies();
    }

    public List<Lofi> getAllLofiesAssignedToLofiPool(long lofiPoolId){
        checkId(lofiPoolId, "lofiPoolId");

        LofiPool lofiPool = this.lofiPoolService.getLofiPoolById(lofiPoolId);
        return lofiPool.getPoolLofies();
    }

    public Lofi saveLofi(Lofi lofi){
        Optional<Lofi> existingLofi = this.lofiRepository.findLofiByName(lofi.getLofiName());

        if(!existingLofi.isPresent()){
            return this.lofiRepository.save(lofi);
        } else {   
            String message = String.format("Cannot save the same Lofi name %s", lofi.getLofiName());
            log.error(message, lofi);
            throw new IllegalArgumentException(message);
        }
    }

    public Lofi updateLofi(Lofi lofi){
        Optional<Lofi> existingLofi = this.lofiRepository.findById(lofi.getLofiId());

        if(existingLofi.isPresent()){
            return this.lofiRepository.save(lofi);
        } else {
            String message = String.format("No such a lofi info. Cannot update the lofiId %d, lofiName %s", 
                lofi.getLofiId(), lofi.getLofiName());
            log.error(message, lofi);
            throw new IllegalArgumentException(message);
        }
    }

    public Lofi deleteLofiById(long lofiId){
        Optional<Lofi> existingLofi = this.lofiRepository.findById(lofiId);

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
