package com.example.project_lofi.lofi;

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
@RequestMapping(path = "api/lofi")
public class LofiController {
    
    private final LofiService lofiService;

    @Autowired
    public LofiController(LofiService lofiService){
        this.lofiService = lofiService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Lofi>> getAllLofies(){
        List<Lofi> retrievedLofiList = this.lofiService.getAllLofies();
        if(retrievedLofiList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedLofiList, HttpStatus.FOUND);
        }
    }

    @GetMapping("/id/{lofiId}")
    @ResponseBody
    public ResponseEntity<Lofi> getLofiById(@PathVariable long lofiId){
        Lofi retrievedLofi = this.lofiService.getLofiById(lofiId);
        if(retrievedLofi == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedLofi, HttpStatus.FOUND);
        }
    }

    @GetMapping("/name/{lofiName}")
    @ResponseBody
    public ResponseEntity<Lofi> getLofiByName(@PathVariable String lofiName){
        Lofi retrievedLofi = this.lofiService.getLofiByName(lofiName);
        if(retrievedLofi == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedLofi, HttpStatus.FOUND);
        }
    }

    @PostMapping(
        path = "/add",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Lofi> saveLofi(@RequestBody Lofi lofi) throws ServerException{
        Lofi savedLofi = this.lofiService.saveLofi(lofi);
        if (savedLofi == null){
            throw new ServerException("Unexpected error occurs while adding a new Lofi, lofiName: "+lofi.getLofiName());
        } else {
            return new ResponseEntity<>(savedLofi, HttpStatus.CREATED);
        }
    }
    
    @PostMapping(
        path = "/update",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Lofi> updateLofi(@RequestBody Lofi lofi) throws ServerException{
        Lofi updatedLofi = this.lofiService.updateLofi(lofi);
        if (updatedLofi == null){
            throw new ServerException("Unexpected error occurs while updated the lofi, lofiName: " + lofi.getLofiName());
        } else {
            return new ResponseEntity<>(updatedLofi, HttpStatus.ACCEPTED);
        }
    }

    @PostMapping(
        path = "/delete/{lofiId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Lofi> deleteLofi(@RequestBody Lofi lofi){
        Lofi deletedLofi = this.lofiService.deleteLofiById(lofi.getLofiId());
        if(deletedLofi == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(deletedLofi, HttpStatus.ACCEPTED);
        }
    }
}
