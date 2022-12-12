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

@RestController
@RequestMapping(path = "/api/lofipool")
public class LofiPoolController {
    private final LofiPoolService lofiPoolService;

    @Autowired
    public LofiPoolController(LofiPoolService lofiPoolService){
        this.lofiPoolService = lofiPoolService;
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
        LofiPool savedLofiPool = this.lofiPoolService.saveLofiPool(lofiPool);

        if (savedLofiPool == null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(savedLofiPool, HttpStatus.CREATED);
        }
    }

    @PostMapping(
        path = "/update",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<LofiPool> updateLofiPool(@RequestBody LofiPool lofiPool) throws ServerException{
        LofiPool updatedLofiPool = this.lofiPoolService.updateLofiPool(lofiPool);

        if(updatedLofiPool == null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(updatedLofiPool, HttpStatus.ACCEPTED);
        }
    }

    @PostMapping(
        path = "/delete",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<LofiPool> deleteLofiPool(@RequestBody LofiPool lofiPool){
        LofiPool deletedLofiPool = this.lofiPoolService.deleteLofiPoolById(lofiPool.getLofiPoolId());

        if(deletedLofiPool == null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }
}
