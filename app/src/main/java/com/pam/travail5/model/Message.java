package com.pam.travail5.model;

import android.annotation.SuppressLint;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.pam.travail5.persistence.Converters;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "messages", indices = {
        @Index(value = {"message", "time", "type"})
})
public class Message {
    public static final int TYPE_INCOMING = 0;
    public static final int TYPE_OUTGOING = 1;

    @TypeConverters({Converters.class})
    private Date time;

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String message;
    private int type;
    private long sessionId;
    private String tag;


    public Message(long id, String message, Date time, int type, long sessionId, String tag) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.type = type;
        this.sessionId = sessionId;
        this.tag = tag;
    }

    @Ignore
    public Message(String message, int type, long sessionId) {
        this.message = message;
        this.type = type;
        this.sessionId = sessionId;
        this.time = new Date();
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Date getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
