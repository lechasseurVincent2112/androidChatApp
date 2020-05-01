package com.pam.travail5.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class MessageSession {

    @Embedded
    private Message message;

    @Relation(
            parentColumn = "sessionId",
            entity = Session.class,
            entityColumn = "id"
    )

    private SessionAvatar sessionAvatar;

    public Message getMessage() {
        return message;
    }

    public SessionAvatar getDetails() {
        return sessionAvatar;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setSessionAvatar(SessionAvatar sessionAvatar) {
        this.sessionAvatar = sessionAvatar;
    }

}
