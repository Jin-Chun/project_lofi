package com.example.project_lofi.lofipool;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class LofiPoolService {
    
    private final LofiPoolRepository lofiPoolRepository;

    @Autowired
    public LofiPoolService(LofiPoolRepository lofiPoolRepository){
        this.lofiPoolRepository = lofiPoolRepository;
    }

    public List<LofiPool> getAllLofiPools(){
        return this.lofiPoolRepository.findAll();
    }

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

    public LofiPool saveLofiPool(LofiPool lofiPool){
        Optional<LofiPool> existingLofiPool = this.lofiPoolRepository.findLofiPoolByName(lofiPool.getLofiPoolName());

        if(!existingLofiPool.isPresent()){
            return this.lofiPoolRepository.save(lofiPool);
        } else {   
            String message = String.format("Cannot save the same LofiPool name %s", lofiPool.getLofiPoolName());
            log.error(message, lofiPool);
            throw new IllegalArgumentException(message);
        }
    }

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
