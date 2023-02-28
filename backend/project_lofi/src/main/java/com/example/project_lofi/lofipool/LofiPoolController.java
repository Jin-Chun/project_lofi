package com.example.project_lofi.lofipool;

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

/**
 * Defines available APIs for lofi pool processes
 * 
 * @author Gwanjin
 */
@RestController @Slf4j
@RequestMapping(path = "/api/lofipool")
public class LofiPoolController {
    private final LofiPoolService lofiPoolService;
    private final LofiPoolAssignmentService lofiPoolAssignmentService;

    @Autowired
    public LofiPoolController(LofiPoolService lofiPoolService, LofiPoolAssignmentService lofiPoolAssignmentService){
        this.lofiPoolService = lofiPoolService;
        this.lofiPoolAssignmentService = lofiPoolAssignmentService;
    }

    /**
     * Retrieve all lofi pools
     * 
     * @return a http response with a list of all lofi pools or {@link HttpStatus#NOT_FOUND} if no lofi pools are found
     */
    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<LofiPool>> getAllLofiPools(){
        List<LofiPool> lofiPools = this.lofiPoolService.getAllLofiPools();

        if(lofiPools.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(lofiPools, HttpStatus.OK);
        }
    }

    /**
     * Retrieve a lofi pool by a given lofi pool identifier
     * 
     * @param lofiPoolId a given lofi pool identifier
     * @return a http response with a lofi pool or {@link HttpStatus#NOT_FOUND} if no matched lofi pool is found
     */
    @GetMapping(path = "/id/{lofiPoolId}")
    @ResponseBody
    public ResponseEntity<LofiPool> getLofiPoolById(@PathVariable long lofiPoolId){
        LofiPool lofiPool = this.lofiPoolService.getLofiPoolById(lofiPoolId);

        if(lofiPool == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(lofiPool, HttpStatus.OK);
        }
    }

    /**
     * Retrieve a lofi pool by a given lofi pool name
     * 
     * @param lofiPoolName a given lofi pool name
     * @return a http response with a lofi pool or {@link HttpStatus#NOT_FOUND} if no matched lofi pool is found
     */
    @GetMapping(path = "/name/{lofiPoolName}")
    @ResponseBody
    public ResponseEntity<LofiPool> getLofiPoolByName(@PathVariable String lofiPoolName){
        LofiPool lofiPool = this.lofiPoolService.getLofiPoolByName(lofiPoolName);

        if(lofiPool == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(lofiPool, HttpStatus.OK);
        }
    }

    /**
     * Save a given lofi pool data
     * 
     * @param lofiPool a given lofi pool data
     * @return a saved lofi pool data or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while saving the lofi pool data
     * @throws ServerException
     */
    @PostMapping(
        path = "/add",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<LofiPool> saveLofiPool(@RequestBody LofiPool lofiPool) throws ServerException{
        try {
            LofiPool savedLofiPool = this.lofiPoolService.saveLofiPool(lofiPool);
            return new ResponseEntity<>(savedLofiPool, HttpStatus.CREATED);
        } catch (Exception e){
            log.error("While saving a lofi pool("+lofiPool.getLofiPoolName()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Update a lofi pool with a given lofi pool data
     * 
     * @param lofiPool a given lofi pool
     * @return an updated lofi pool data or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while updating the lofi pool data
     * @throws ServerException
     */
    @PostMapping(
        path = "/update",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<LofiPool> updateLofiPool(@RequestBody LofiPool lofiPool) throws ServerException{
        try {
            LofiPool updatedLofiPool = this.lofiPoolService.updateLofiPool(lofiPool);
            return new ResponseEntity<>(updatedLofiPool, HttpStatus.OK);
        } catch (Exception e){
            log.error("While updating a lofi pool("+lofiPool.getLofiPoolId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Delete a lofi pool that matches to a given lofi data
     * 
     * @param lofiPool a given lofi pool data
     * @return a deleted lofi pool data or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while deleting the lofi pool data
     */
    @PostMapping(
        path = "/delete",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<LofiPool> deleteLofiPool(@RequestBody LofiPool lofiPool){
        try {
            this.lofiPoolService.deleteLofiPoolById(lofiPool.getLofiPoolId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            log.error("While deleting a lofi pool("+lofiPool.getLofiPoolId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Assign a lofi to a lofi pool by using given lofi id and lofi pool id
     * 
     * @param lofiId a given lofi id
     * @param lofiPoolId a given lofi pool id
     * @return a updated lofi pool data or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while assigning a lofi to a lofi pool
     */
    @PostMapping(
        path = "/assign/{lofiId}/to/{lofiPoolId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<LofiPool> assignLofiToLofiPool(@PathVariable long lofiId, @PathVariable long lofiPoolId){
        try {
            LofiPool updatedLofiPool = this.lofiPoolAssignmentService.assignLofiToLofiPool(lofiId, lofiPoolId);
            return new ResponseEntity<>(updatedLofiPool, HttpStatus.OK);
        } catch (Exception e){
            log.error("While assigning a lofi("+lofiId+") to a lofi pool("+lofiPoolId+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }        
    }

    /**
     * Remove a lofi from a lofi pool by using given lofi id and lofi pool id
     * 
     * @param lofiId a given lofi id
     * @param lofiPoolId a given lofi pool id
     * @return a updated lofi pool data or {@link HttpStatus#NOT_ACCEPTABLE} if error occurs while removing a lofi from a lofi pool
     */
    @PostMapping(
        path = "/remove/{lofiId}/from/{lofiPoolId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<LofiPool> removeLofiFromLofiPool(@PathVariable long lofiId, @PathVariable long lofiPoolId){
        try {
            LofiPool updatedLofiPool = this.lofiPoolAssignmentService.removeLofiFromLofiPool(lofiId, lofiPoolId);
            return new ResponseEntity<>(updatedLofiPool, HttpStatus.OK);
        } catch (Exception e){
            log.error("While removing a lofi("+lofiId+") from a lofi pool("+lofiPoolId+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
