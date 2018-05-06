package com.company.domain.services.cache.controllers;

import com.company.domain.services.cache.dto.CacheDto;
import com.company.domain.services.cache.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * REST API for in memory caching
 *
 * @author Lakshman Bhupathi
 */
@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    CacheService cacheService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public boolean add(@Valid @RequestBody CacheDto cacheDto) {
        return cacheService.add(cacheDto);
    }

    @RequestMapping(value = "/peek", method = RequestMethod.GET)
    public Object peek() {
        return cacheService.peek();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public boolean remove(@Valid @RequestBody CacheDto cacheDto) {
        return cacheService.delete(cacheDto);
    }

    @RequestMapping(value = "/take", method = RequestMethod.GET)
    public Object take() {
        return cacheService.take();
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
