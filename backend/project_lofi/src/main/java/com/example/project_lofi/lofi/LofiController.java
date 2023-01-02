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

import lombok.extern.slf4j.Slf4j;

@RestController @Slf4j
@RequestMapping(path = "api/lofi")
public class LofiController {
    
    private final LofiService lofiService;

    @Autowired
    public LofiController(LofiService lofiService){
        this.lofiService = lofiService;
    }

    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<Lofi>> getAllLofies(){
        List<Lofi> retrievedLofiList = this.lofiService.getAllLofies();
        if(retrievedLofiList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedLofiList, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/id/{lofiId}")
    @ResponseBody
    public ResponseEntity<Lofi> getLofiById(@PathVariable long lofiId){
        Lofi retrievedLofi = this.lofiService.getLofiById(lofiId);
        if(retrievedLofi == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedLofi, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/name/{lofiName}")
    @ResponseBody
    public ResponseEntity<Lofi> getLofiByName(@PathVariable String lofiName){
        Lofi retrievedLofi = this.lofiService.getLofiByName(lofiName);
        if(retrievedLofi == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(retrievedLofi, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/type/{lofiType}")
    @ResponseBody
    public ResponseEntity<List<Lofi>> getLofiesByType(@PathVariable String lofiType){
        List<Lofi> retrievedLofies = this.lofiService.getLofiesByType(lofiType);

        if(retrievedLofies != null && !retrievedLofies.isEmpty()){
            return new ResponseEntity<>(retrievedLofies, HttpStatus.OK); 
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/keyword/{lofiKeyword}")
    @ResponseBody
    public ResponseEntity<List<Lofi>> getLofiesByKeyword(@PathVariable String lofiKeyword){
        List<Lofi> retrievedLofies = this.lofiService.getLofiesByKeyword(lofiKeyword);

        if(retrievedLofies != null && !retrievedLofies.isEmpty()){
            return new ResponseEntity<>(retrievedLofies, HttpStatus.OK); 
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/playlistid/{playlistId}")
    @ResponseBody
    public ResponseEntity<List<Lofi>> getAllLofiesAssignedToPlaylist(@PathVariable long playlistId){
        List<Lofi> retrievedLofies = this.lofiService.getAllLofiesAssignedToPlaylist(playlistId);
        if(retrievedLofies != null && !retrievedLofies.isEmpty()){
            return new ResponseEntity<>(retrievedLofies, HttpStatus.OK); 
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/lofipoolid/{lofiPoolId}")
    @ResponseBody
    public ResponseEntity<List<Lofi>> getAllLofiesAssignedToLofiPool(@PathVariable long lofiPoolId){
        List<Lofi> retrievedLofies = this.lofiService.getAllLofiesAssignedToLofiPool(lofiPoolId);
        if(retrievedLofies != null && !retrievedLofies.isEmpty()){
            return new ResponseEntity<>(retrievedLofies, HttpStatus.OK); 
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(
        path = "/add",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Lofi> saveLofi(@RequestBody Lofi lofi) throws ServerException{
        try{
            Lofi savedLofi = this.lofiService.saveLofi(lofi);
            return new ResponseEntity<>(savedLofi, HttpStatus.CREATED);
        } catch (Exception e){
            log.error("While saving a lofi("+lofi.getLofiId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
    
    @PostMapping(
        path = "/update",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Lofi> updateLofi(@RequestBody Lofi lofi) throws ServerException{
        try{
            Lofi updatedLofi = this.lofiService.updateLofi(lofi);
            return new ResponseEntity<>(updatedLofi, HttpStatus.OK);
        } catch (Exception e){
            log.error("While updating a lofi("+lofi.getLofiId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(
        path = "/delete",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Lofi> deleteLofi(@RequestBody Lofi lofi){
        try{
            this.lofiService.deleteLofiById(lofi.getLofiId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            log.error("While deleting a lofi("+lofi.getLofiId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
