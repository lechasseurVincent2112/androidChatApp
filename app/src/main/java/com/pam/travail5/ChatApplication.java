package com.pam.travail5;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import com.pam.travail5.persistence.ChatDatabase;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatApplication extends Application {
    public static final String URL = "https://calma-420-4n2-aa.herokuapp.com";
    public static final String UUID = "uuid";
    public static final String CONFIGS = "configs";
    public static final String CHAT_DATABASE_DB = "chat-database.db";
    private Socket socket;
    private ChatDatabase chatDataBase;

    public ChatApplication() {
        try {
            socket = IO.socket(URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public ChatDatabase getDataBase() {
        if (chatDataBase == null || !chatDataBase.isOpen()) {
            chatDataBase = Room.databaseBuilder(this, ChatDatabase.class,
                    CHAT_DATABASE_DB).fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }
        return chatDataBase;
    }

    public String getUuid(){
        SharedPreferences prefs = getSharedPreferences(CONFIGS, Context.MODE_PRIVATE);
        String uuid = prefs.getString(UUID, null);
        if (uuid == null){
            uuid = java.util.UUID.randomUUID().toString();
            prefs.edit().putString(UUID, uuid).apply();
        }
        return uuid;
    }

    public Socket getSocket() {
        return socket;
    }
}
