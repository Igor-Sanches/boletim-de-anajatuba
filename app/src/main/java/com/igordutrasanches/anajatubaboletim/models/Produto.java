package com.igordutrasanches.anajatubaboletim.models;

import java.util.UUID;

public class Produto {
private long id, position;
private String name, uid, tokem;

    public Produto(String name, String uid, long position) {
        this.position = position;
        this.name = name;
        this.uid = uid;
        this.tokem = UUID.randomUUID().toString();
    }

    public String getTokem() {
        return tokem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
