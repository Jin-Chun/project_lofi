package com.example.project_lofi.lofi;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class LofiService {
    
    private final LofiRepository lofiRepository;

    @Autowired
    public LofiService(LofiRepository lofiRepository){
        this.lofiRepository = lofiRepository;
    }

    public Lofi getLofiById(long lofiId){
        Optional<Lofi> optionalLofi = this.lofiRepository.findById(lofiId);
        if (optionalLofi.isPresent()){
            return optionalLofi.get();
        } else {
            String message = String.format("No such a lofi identifier %d is found", lofiId);
            log.error(message, lofiId);
            throw new IllegalArgumentException(message);
        }
    }

    public List<Lofi> getAllLofies(){
        return this.lofiRepository.findAll();
    }

    public Lofi saveLofi(Lofi lofi){

        Optional<Lofi> existingLofi = this.lofiRepository.findLofiByLofiName(lofi.getLofiName());

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

    public Lofi getLofiByName(String lofiName){
        Optional<Lofi> optionalLofi = this.lofiRepository.findLofiByLofiName(lofiName);
        if (optionalLofi.isPresent()){
            return optionalLofi.get();
        } else {
            String message = String.format("No such a lofi name %s is found", lofiName);
            log.error(message, lofiName);
            return null;
        }
    }
}
