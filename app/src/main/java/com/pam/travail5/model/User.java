package com.pam.travail5.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users", indices = {@Index(value = {"uuid"}, unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String uuid;

    public User(long id, String uuid) {
        this.id = id;
        this.uuid = uuid;
    }

    @Ignore
    public User(String uuid) {
        this.uuid = uuid;
    }

    public long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }
}
