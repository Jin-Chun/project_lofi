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

    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<LofiPool>> getAllLofiPools(){
        List<LofiPool> lofiPools = this.lofiPoolService.getAllLofiPools();

        if(lofiPools.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(lofiPools, HttpStatus.FOUND);
        }
    }

    @GetMapping(path = "/id/{lofiPoolId}")
    @ResponseBody
    public ResponseEntity<LofiPool> getLofiPoolById(@PathVariable long lofiPoolId){
        LofiPool lofiPool = this.lofiPoolService.getLofiPoolById(lofiPoolId);

        if(lofiPool == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(lofiPool, HttpStatus.FOUND);
        }
    }

    @GetMapping(path = "/name/{lofiPoolName}")
    @ResponseBody
    public ResponseEntity<LofiPool> getLofiPoolByName(@PathVariable String lofiPoolName){
        LofiPool lofiPool = this.lofiPoolService.getLofiPoolByName(lofiPoolName);

        if(lofiPool == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(lofiPool, HttpStatus.FOUND);
        }
    }

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

    @PostMapping(
        path = "/update",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<LofiPool> updateLofiPool(@RequestBody LofiPool lofiPool) throws ServerException{
        try {
            LofiPool updatedLofiPool = this.lofiPoolService.updateLofiPool(lofiPool);
            return new ResponseEntity<>(updatedLofiPool, HttpStatus.ACCEPTED);
        } catch (Exception e){
            log.error("While updating a lofi pool("+lofiPool.getLofiPoolId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(
        path = "/delete",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<LofiPool> deleteLofiPool(@RequestBody LofiPool lofiPool){
        try {
            this.lofiPoolService.deleteLofiPoolById(lofiPool.getLofiPoolId());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e){
            log.error("While deleting a lofi pool("+lofiPool.getLofiPoolId()+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(
        path = "/assign/{lofiId}/to/{lofiPoolId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<LofiPool> assignLofiToLofiPool(@PathVariable long lofiId, @PathVariable long lofiPoolId){
        try {
            LofiPool updatedLofiPool = this.lofiPoolAssignmentService.assignLofiToLofiPool(lofiId, lofiPoolId);
            return new ResponseEntity<>(updatedLofiPool, HttpStatus.ACCEPTED);
        } catch (Exception e){
            log.error("While assigning a lofi("+lofiId+") to a lofi pool("+lofiPoolId+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }        
    }

    @PostMapping(
        path = "/remove/{lofiId}/from/{lofiPoolId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<LofiPool> removeLofiFromLofiPool(@PathVariable long lofiId, @PathVariable long lofiPoolId){
        try {
            LofiPool updatedLofiPool = this.lofiPoolAssignmentService.removeLofiFromLofiPool(lofiId, lofiPoolId);
            return new ResponseEntity<>(updatedLofiPool, HttpStatus.ACCEPTED);
        } catch (Exception e){
            log.error("While removing a lofi("+lofiId+") from a lofi pool("+lofiPoolId+"), unexpected error occurs", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
