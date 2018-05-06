package com.company.domain.services.cache.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception for {@link com.company.domain.services.cache.map.ConcurrentCacheMap}.take()
 */
@ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT, reason = "Cache is Empty")
public class CacheEmptyTimeOutException extends RuntimeException{
}
