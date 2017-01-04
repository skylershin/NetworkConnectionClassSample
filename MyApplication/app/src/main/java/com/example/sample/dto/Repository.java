package com.example.sample.dto;

/**
 * Created by MunkyuShin on 1/4/17.
 */

public class Repository {
    private String name;

    @Override
    public String toString() {
        return "name: " + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
