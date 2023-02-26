package com.example.project_lofi;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

/**
 * Defines common functions for all service classes that extends.
 * 
 * @author Gwanjin
 */
@Slf4j
public abstract class AbstractService {

    /**
     * Check if a given object is null. If so, throws {@link IllegalArgumentException}
     * 
     * @param object a given object
     * @param tag a brief description of the object
     */
    protected void checkNull(Object object, String tag){
        if(object == null){
            String message = String.format("A given %s cannot be null", tag);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Check if a given object is null or Empty. If it is, throws {@link IllegalArgumentException}
     * 
     * @param object a given object
     * @param tag a brief description of the object
     */
    protected void checkNullAndEmpty(Object object, String tag){
        checkNull(object, tag);

        if (object instanceof Collection){
            Collection aList =  (Collection) object;
            if (aList.isEmpty()){
                String message = String.format("A given %s cannot be empty", tag);
                log.error(message);
                throw new IllegalArgumentException(message);
            }
        }
    }

    /**
     * Check if a given object id is valid, If it is less than 0, throws {@link IllegalArgumentException}
     * 
     * @param objectId a given object Id
     * @param tag a brief description of the Id
     */
    protected void checkId(long objectId, String tag){
        if(objectId < 0){
            String message = String.format("A given %s is invalid", tag);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Check if a given number is valid, If it is less than 1, throws {@link IllegalArgumentException}
     * 
     * @param num a given number
     */
    protected void checkMinNum(int num){
        if(num < 1){
            String message = String.format("A given %d is less than minimum number(1)", num);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}
