package com.github.task.tarantool.demo.services;

import com.github.task.tarantool.demo.model.rest.KVObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KVService {

    Logger logger = LoggerFactory.getLogger(KVService.class);

    private final TarantoolStorageService storageService;

    public KVService(TarantoolStorageService storageService) {
        this.storageService = storageService;
    }

    public boolean deleteKVObject(String key) {
        return storageService.deleteKVObject(key);
    }

    public KVObject updateKVObject(String key, String value) {
        return storageService.updateKVObject(key, value);
    }

    public KVObject createKVObject(String key, String value) {
        KVObject object = getKVObject(key);
        if (object != null) {
            return updateKVObject(key, value);
        }
        return storageService.createKVObject(key, value);
    }

    public KVObject getKVObject(String key) {
        return storageService.getKVObjectByKey(key);
    }

    public Integer getKVObjectsCount(){
        return storageService.getObjectsCount();
    }
}
