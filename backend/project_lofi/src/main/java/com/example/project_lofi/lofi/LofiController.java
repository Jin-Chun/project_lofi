package com.example.project_lofi.lofi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/lofi")
public class LofiController {
    
    private final LofiService lofiService;

    @Autowired
    public LofiController(LofiService lofiService){
        this.lofiService = lofiService;
    }

    @GetMapping
    public List<Lofi> getAllLofies(){
        return this.lofiService.getAllLofies();
    }

    @PostMapping
    public void saveLofi(){
        
    }
    
}
