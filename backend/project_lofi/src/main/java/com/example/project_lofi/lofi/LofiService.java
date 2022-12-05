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

    public Lofi getLofi(long lofiId){
        Optional<Lofi> lofiOptional = this.lofiRepository.findById(lofiId);
        if (lofiOptional.isPresent()){
            return lofiOptional.get();
        } else {
            String message = String.format("No such a lofi identifier %d is found", lofiId);
            log.error(message, lofiId);
            throw new IllegalArgumentException(message);
        }
    }

    public List<Lofi> getAllLofies(){
        return this.lofiRepository.findAll();
    }

    public void saveLofi(Lofi lofi){

        Lofi existingLofi = this.lofiRepository.findLofiByLofiName(lofi.getLofiName());

        if (existingLofi != null){
            String message = String.format("Cannot save the same Lofi name %s", lofi.getLofiName());
            log.error(message, lofi);
            throw new IllegalArgumentException(message);
        } else {
            this.lofiRepository.save(lofi);
        }
    }
}
