package com.example.project_lofi.lofipool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_lofi.AbstractService;
import com.example.project_lofi.lofi.Lofi;
import com.example.project_lofi.lofi.LofiService;

import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class LofiPoolAssignmentService extends AbstractService {
    @Autowired
    private LofiPoolService lofiPoolService;
    @Autowired
    private LofiService lofiService;
    

    public LofiPool assignLofiToLofiPool(long lofiId, long lofiPoolId){
        
        // #1. verify a given lofi id and lofiPool id exist
        Lofi lofi = this.lofiService.getLofiById(lofiId);
        LofiPool lofiPool = this.lofiPoolService.getLofiPoolById(lofiPoolId);

        checkNull(lofi, "lofi");
        checkNull(lofiPool, "lofiPool");

        // #2. add the lofi to the poolLofies
        lofiPool.getPoolLofies().add(lofi);

        // #3. save the LofiPool
        LofiPool updatedLofiPool = this.lofiPoolService.updateLofiPool(lofiPool);
        log.info(String.format("A given lofi (lofiId: %d) has been assigned to the lofiPool(lofiPoolId: %d)", lofiId, lofiPoolId));
        
        // #4. return the LofiPool
        return updatedLofiPool;
    }

    public LofiPool removeLofiFromLofiPool(long lofiId, long lofiPoolId){
        // #1. verify a given lofi id and lofi pool id exist
        Lofi lofi = this.lofiService.getLofiById(lofiId);
        LofiPool lofiPool = this.lofiPoolService.getLofiPoolById(lofiPoolId);

        checkNull(lofi, "lofi");
        checkNull(lofiPool, "lofiPool");

        // #2. remove the lofi from the pool lofies
        if (lofiPool.getPoolLofies().remove(lofi)){
            if (lofi.getLofiPools() != null){
                lofi.getLofiPools().remove(lofiPool);
            }
            log.info(String.format("A given lofi (lofiId: %d) has been removed from the lofiPool(lofiPoolId: %d)", lofiId, lofiPoolId));
        } else {
            log.info(String.format("A given lofi (lofiId: %d) has not been assigned to the lofiPool(lofiPoolId: %d)", lofiId, lofiPoolId));
        }

        // #3. save the LofiPool
        LofiPool updatedLofiPool = this.lofiPoolService.updateLofiPool(lofiPool);
        
        // #4. return the LofiPool
        return updatedLofiPool;
    }
}
