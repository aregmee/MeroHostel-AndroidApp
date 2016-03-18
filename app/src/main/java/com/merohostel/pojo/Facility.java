package com.merohostel.pojo;

/**
 * Created by asimsangram on 2/16/16.
 */
public class Facility {

    private int id;
    private String name;

    public Facility(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
