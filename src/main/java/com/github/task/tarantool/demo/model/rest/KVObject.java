package com.github.task.tarantool.demo.model.rest;

import java.util.Objects;

public class KVObject {

    private final String key;
    private final String value;

    public KVObject(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KVObject kvObject = (KVObject) o;
        return Objects.equals(key, kvObject.key) && Objects.equals(value, kvObject.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "KVObject{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
