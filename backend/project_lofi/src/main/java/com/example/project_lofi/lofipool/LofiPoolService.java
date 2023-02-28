package com.example.project_lofi.lofipool;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Defines logics for the lofi pool data process 
 * 
 * @author Gwanjin
 */
@Service @Slf4j
public class LofiPoolService {
    
    @Autowired
    private LofiPoolRepository lofiPoolRepository;

    /**
     * Retrieve all lofi pools
     * 
     * @return a list of all lofi pools or empty list
     */
    public List<LofiPool> getAllLofiPools(){
        return this.lofiPoolRepository.findAll();
    }

    /**
     * Retrieve a lofi pool by a given lofi pool identifier
     * 
     * @param lofiPoolId a given lofi pool id
     * @return a matched lofi pool or {@link IllegalArgumentException} if no matched lofi pool exists
     */
    public LofiPool getLofiPoolById(long lofiPoolId){
        Optional<LofiPool> existingLofiPool = this.lofiPoolRepository.findById(lofiPoolId);
        if (existingLofiPool.isPresent()){
            return existingLofiPool.get();
        } else {
            String message = String.format("No such a LofiPool identifier %d is found", lofiPoolId);
            log.error(message, lofiPoolId);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Retrieve a lofi pool by a given lofi pool name
     * 
     * @param lofiPoolName a given lofi pool name
     * @return a matched lofi pool or {@link IllegalArgumentException} if no matched lofi pool exists
     */
    public LofiPool getLofiPoolByName(String lofiPoolName){
        Optional<LofiPool> optionalLofiPool = this.lofiPoolRepository.findLofiPoolByName(lofiPoolName);
        if (optionalLofiPool.isPresent()){
            return optionalLofiPool.get();
        } else {
            String message = String.format("No such a lofi pool name %s is found", lofiPoolName);
            log.error(message, lofiPoolName);
            return null;
        }
    }

    /**
     * Save a given lofi pool data
     * 
     * @param lofiPool a given lofi pool data
     * @return a saved lofi pool or {@link IllegalArgumentException} if there is a matched lofi pool with the given lofi pool data
     */
    public LofiPool saveLofiPool(LofiPool lofiPool){
        Optional<LofiPool> existingLofiPool = this.lofiPoolRepository.findLofiPoolByName(lofiPool.getLofiPoolName());

        if(!existingLofiPool.isPresent()){
            lofiPool.setLofiPoolId(null);
            return this.lofiPoolRepository.save(lofiPool);
        } else {   
            String message = String.format("Cannot save the same LofiPool name %s", lofiPool.getLofiPoolName());
            log.error(message, lofiPool);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Update a lofi pool with a given lofi pool data
     * 
     * @param lofiPool a given lofi pool
     * @return an updated lofi pool or {@link IllegalArgumentException} if there is no matched lofi pool with the given lofi pool data
     */
    public LofiPool updateLofiPool(LofiPool lofiPool){
        Optional<LofiPool> existingLofiPool = this.lofiPoolRepository.findById(lofiPool.getLofiPoolId());

        if(existingLofiPool.isPresent()){
            return this.lofiPoolRepository.save(lofiPool);
        } else {
            String message = String.format("No such a lofi pool info. Cannot update the LofiPool of lofiPoolId %d, lofiPoolName %s", 
            lofiPool.getLofiPoolId(), lofiPool.getLofiPoolName());
            log.error(message, lofiPool);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Delete a lofi pool by using a given lofi pool id
     * 
     * @param lofiPoolId a given lofi pool id
     * @return a deleted lofi pool data or {@link IllegalArgumentException} if there is no matched lofi pool with a given lofi pool data
     */
    public LofiPool deleteLofiPoolById(long lofiPoolId){
        Optional<LofiPool> existingLofiPool = this.lofiPoolRepository.findById(lofiPoolId);

        if(existingLofiPool.isPresent()){
            LofiPool lofiPoolToBeDeleted = existingLofiPool.get();
            this.lofiPoolRepository.delete(existingLofiPool.get());
            return lofiPoolToBeDeleted;
        } else {
            String message = String.format("No such a lofi pool info. Cannot delete the Lofi pool, the given lofi pool identifier: %d", 
                lofiPoolId);
            log.error(message, lofiPoolId);
            throw new IllegalArgumentException(message);
        }
    }
}
