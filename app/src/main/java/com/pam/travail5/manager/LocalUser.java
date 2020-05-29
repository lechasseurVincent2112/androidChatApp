package com.pam.travail5.manager;


import com.pam.travail5.model.Message;

public class LocalUser extends UserHolder {

    public LocalUser(String uuid, DatabaseManager databaseManager) {
        super(uuid, databaseManager);
    }

    @Override
    public Message newMessage(String content, String tag) {
        if (tag == ""){
            return getDatabaseManager().newMessage(getUuid(), content, com.pam.travail5.model.Message.TYPE_OUTGOING, "");
        } else {
            return getDatabaseManager().newMessage(getUuid(), content, com.pam.travail5.model.Message.TYPE_OUTGOING, tag);

        }
    }

    @Override
    public UserHolder setUsername(String username) {
        return super.setUsername(username);
    }
}
