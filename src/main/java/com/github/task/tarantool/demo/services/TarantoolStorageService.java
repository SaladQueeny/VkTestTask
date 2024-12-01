package com.github.task.tarantool.demo.services;

import com.github.task.tarantool.demo.model.rest.KVObject;
import io.tarantool.driver.api.TarantoolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class TarantoolStorageService {

    Logger logger = LoggerFactory.getLogger(TarantoolStorageService.class);

    private final TarantoolClient tarantoolClient;

    public TarantoolStorageService(TarantoolClient tarantoolClient) {
        this.tarantoolClient = tarantoolClient;
    }

    public KVObject getKVObjectByKey(String key) {
        List<Object> userTuple = null;
        try {
            userTuple = (List<Object>) tarantoolClient.call("get_kv_object_by_key", key).get().get(0);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if ((userTuple != null) && !userTuple.isEmpty()) {
            KVObject kvObject = new KVObject((String) userTuple.get(1), (String) userTuple.get(2));

            return kvObject;
        }

        return null;
    }

    public KVObject createKVObject(String key, String value) {
        logger.info("Create in tarantoolService");
        List<Object> userTuple = null;
        try {
            userTuple = (List<Object>) tarantoolClient.call("create_kv_object",
                    key,
                    value
            ).get().get(0);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        if ((userTuple != null) && !userTuple.isEmpty()) {
            KVObject kvObject = new KVObject((String) userTuple.get(1), (String) userTuple.get(2));

            return kvObject;
        }

        return null;
    }

    public boolean deleteKVObject(String key) {
        Object res = null;
        try {
            res = tarantoolClient.call("delete_kv_object_by_key", key).get().get(0);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return res != null;
    }

    public KVObject updateKVObject(String key, String value) {
        List<Object> userTuple = null;
        try {
            userTuple = (List<Object>) tarantoolClient.call("update_kv_object",
                    key,
                    value
            ).get().get(0);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if ((userTuple != null) && !userTuple.isEmpty()) {

            return new KVObject((String) userTuple.get(1), (String) userTuple.get(2));
        }

        return null;
    }

    public Integer getObjectsCount() {
        Object res = null;
        try {
            res = tarantoolClient.call("get_count_of_kv_space").get().get(0);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (res == null) {
            return 0;
        }
        return (Integer) res;
    }

}
