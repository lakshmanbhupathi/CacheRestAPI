package com.company.domain.services.cache.controllers;

import com.company.domain.services.cache.dto.CacheDto;
import com.company.domain.services.cache.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    public boolean add(@RequestBody CacheDto cacheDto){
        return cacheService.add(cacheDto);
    }

    @RequestMapping(value = "/peek",method = RequestMethod.GET)
    public Object peek(){
        return cacheService.peek();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public boolean remove(@RequestBody CacheDto cacheDto) {
        return cacheService.delete(cacheDto);
    }

    @RequestMapping(value = "/take", method = RequestMethod.GET)
    public Object take(){
        try {
            return cacheService.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
