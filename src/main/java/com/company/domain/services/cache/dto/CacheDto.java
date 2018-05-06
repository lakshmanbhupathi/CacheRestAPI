package com.company.domain.services.cache.dto;

import javax.validation.constraints.NotNull;

public class CacheDto {
    @NotNull
    private String key;
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
