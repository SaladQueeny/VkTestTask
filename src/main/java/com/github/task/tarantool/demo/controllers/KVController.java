package com.github.task.tarantool.demo.controllers;

import com.github.task.tarantool.demo.model.rest.KVObject;
import com.github.task.tarantool.demo.services.KVService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class KVController {

    private final KVService KVService;
    Logger logger = LoggerFactory.getLogger(KVController.class);

    public KVController(KVService KVService) {
        this.KVService = KVService;
    }

    @PostMapping(value = "/kv", produces = {"application/json"})
    public ResponseEntity<KVObject> createKVObject(@RequestParam String key, @RequestParam String value) {
        KVObject result = KVService.createKVObject(key, value);
        if (result != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(result);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/kv/{key}", produces = {"application/json"})
    public ResponseEntity<KVObject> getKVObject(@PathVariable("key") String key) {
        KVObject resp = KVService.getKVObject(key);
        if (resp != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(resp);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/kv", produces = {"application/json"})
    public ResponseEntity<Integer> getKVObjectCount() {
        Integer resp = KVService.getKVObjectsCount();
        if (resp != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(resp);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/kv/{key}")
    public ResponseEntity<Void> deleteKVObject(@PathVariable("key") String key) {
        boolean updated = KVService.deleteKVObject(key);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
