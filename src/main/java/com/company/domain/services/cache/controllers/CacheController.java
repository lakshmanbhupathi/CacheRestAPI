package com.company.domain.services.cache.controllers;

import com.company.domain.services.cache.dto.CacheObject;
import com.company.domain.services.cache.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @RequestMapping("/test")
    public String test(){
        return "Lakshman";
    }

    @RequestMapping(value = "/add" , method = RequestMethod.POST)
    public boolean add(CacheObject cacheObject){
        return cacheService.add(cacheObject);
    }

    @RequestMapping(value = "/peek",method = RequestMethod.GET)
    public CacheObject peek(){
        return cacheService.peek();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public boolean remove(CacheObject cacheObject) {
        return cacheService.delete(cacheObject);
    }

    @RequestMapping(value = "/take", method = RequestMethod.GET)
    public CacheObject take(){
        try {
            return cacheService.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
