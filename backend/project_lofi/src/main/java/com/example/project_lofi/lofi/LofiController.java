package com.example.project_lofi.lofi;

import java.rmi.ServerException;
import java.util.ArrayList;
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

/**
 * Defines available APIs for Lofi data
 * 
 * @author Gwanjin
 */
@RestController @Slf4j
@RequestMapping(path = "api/lofi")
public class LofiController {
    
    private final LofiService lofiService;

    @Autowired
    public LofiController(LofiService lofiService){
        this.lofiService = lofiService;
    }

    /**
     * Retrieves all stored lofies
     * 
     * @return a http response with a list of all lofies or {@link HttpStatus#NOT_FOUND} if no lofies are found
     */
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

    /**
     * Retrieves a lofi by a given lofi Identifier
     * 
     * @param lofiId a given lofi identifier
     * @return a http response with a matched lofi or {@link HttpStatus#NOT_FOUND} if no matched lofi is found
     */
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

    /**
     * Retreives a lofi by a given lofi name
     * 
     * @param lofiName a given lofi name
     * @return a http response with a matched lofi or {@link HttpStatus#NOT_FOUND} if no matched lofi is found
     */
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

    /**
     * Retrieves all matched lofies with a given lofi type
     * 
     * @param lofiType a given lofi type
     * @return a http respones with a list of matched lofies or {@link HttpStatus#NOT_FOUND} if no matched lofies are found
     */
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

    /**
     * Retrieves lofies by a given keyword of lofi names
     * 
     * @param lofiKeyword a given keyword
     * @return a http response with a list of all matched lofies that have the keyword in their names or {@link HttpStatus#NOT_FOUND} if no matched lofies are found
     */
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

    /**
     * Retrieves all lofies that have been assigned to a playlist which id matches a given playlist id
     * 
     * @param playlistId a given playlist identifier
     * @return a http response with a list of all lofies assigned to a playlist which id matches a given playlist Id or {@link HttpStatus#NOT_FOUND} if a playlist has no lofies
     */
    @GetMapping(path = "/playlistid/{playlistId}")
    @ResponseBody
    public ResponseEntity<List<Lofi>> getAllLofiesAssignedToPlaylist(@PathVariable long playlistId){
        List<Lofi> retrievedLofies = this.lofiService.getAllLofiesAssignedToPlaylist(playlistId);
        if(retrievedLofies != null && !retrievedLofies.isEmpty()){
            return new ResponseEntity<>(retrievedLofies, HttpStatus.OK); 
        } else {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }

    /**
     * Retrieves all lofies that a user has by a given user Id
     * 
     * @param userId a given user id
     * @return a http response with a list of all lofies that a user has or {@link HttpStatus#NOT_FOUND} if a user has no lofies
     */
    @GetMapping(path = "/userid/{userId}")
    @ResponseBody
    public ResponseEntity<List<Lofi>> getAllLofiesByUserId(@PathVariable long userId){
        List<Lofi> retrievedLofies = this.lofiService.getAllLofiesByUserId(userId);
        if(retrievedLofies != null && !retrievedLofies.isEmpty()){
            return new ResponseEntity<>(retrievedLofies, HttpStatus.OK); 
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves all lofies that have been assigned to a lofi pool which id matches a given lofi pool id
     * 
     * @param lofiPoolId a given lofi pool id
     * @return a http response with a list of all lofies that a lofi pool has
     */
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

    /**
     * Saves a given lofi data
     * 
     * @param lofi a given lofi data
     * @return saved lofi data or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while saving the lofi data
     * @throws ServerException
     */
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
    
    /**
     * Updates a given lofi data
     * 
     * @param lofi a given lofi data
     * @return updated lofi data or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while updating the lofi data
     * @throws ServerException
     */
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

    /**
     * Deletes a given lofi data
     * 
     * @param lofi a given lofi data
     * @return {@link HttpStatus#OK} or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while deleting the lofi data
     */
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
