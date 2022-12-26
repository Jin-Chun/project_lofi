package com.example.project_lofi;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractService {

    protected void checkNull(Object object, String tag){
        if(object == null){
            String message = String.format("A given %s cannot be null", tag);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    protected void checkNullAndEmpty(Object object, String tag){
        if (object instanceof Collection){
            Collection aList =  (Collection) object;
            if (aList == null || aList.isEmpty()){
                String message = String.format("A given %s cannot be null or empty", tag);
                log.error(message);
                throw new IllegalArgumentException(message);
            }
        } else {
            if(object == null){
                String message = String.format("A given %s cannot be null", tag);
                log.error(message);
                throw new IllegalArgumentException(message);
            }
        }
    }

    protected void checkId(long objectId, String tag){
        if(objectId < 0){
            String message = String.format("A given %s is invalid", tag);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    protected void checkMinNum(int num){
        if(num < 1){
            String message = String.format("A given %d is less than minimum number(1)", num);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}
