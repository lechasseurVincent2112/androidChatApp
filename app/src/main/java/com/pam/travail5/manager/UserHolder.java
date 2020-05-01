package com.pam.travail5.manager;

import android.graphics.Bitmap;

import com.pam.travail5.model.Avatar;
import com.pam.travail5.model.Message;
import com.pam.travail5.model.Session;
import com.pam.travail5.persistence.Converters;

import java.util.Date;

abstract class UserHolder {

    private Session session;
    private Avatar avatar;

    protected String uuid;
    protected DatabaseManager databaseManager;

    protected String username;
    protected String avatarBase64;
    protected Bitmap avatarBitmap;
    protected Date loginTime;

    public UserHolder(String uuid, DatabaseManager databaseManager) {
        this.uuid = uuid;
        this.databaseManager = databaseManager;
    }

    public Session newSession() {
        session = getDatabaseManager().newSession(
                getUuid(),
                getUsername(),
                getAvatar(),
                getLoginTime()
        );
        avatar = null;
        return session;
    }

    public abstract Message newMessage(String content);

    public UserHolder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserHolder setAvatar(String avatarBase64) {
        this.avatarBase64 = avatarBase64;
        this.avatarBitmap = null;
        this.avatar = null;
        return this;
    }

    public UserHolder setAvatar(Bitmap avatarBitmap) {
        this.avatarBitmap = avatarBitmap;
        this.avatarBase64 = null;
        this.avatar = null;
        return this;
    }

    public UserHolder setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
        return this;
    }

    public Session getSession() {
        if (session == null) {
            session = getDatabaseManager().getSession(uuid);
        }
        return session;
    }

    public Avatar getAvatar() {
        if (avatarBitmap != null) {
            avatar = new Avatar(avatarBitmap);
        } else if (avatarBase64 != null) {
            avatar = new Avatar(Converters.convert(avatarBase64));
        } else if (avatar != null) {
            avatar = getDatabaseManager().getAvatar(getUuid());
        }
        return avatar;
    }

    public String getUuid() {
        return uuid;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public String getUsername() {
        String username = this.username;
        if (username == null) {
            Session session = getSession();
            if (session != null) {
                username = session.getUsername();
            }
        }
        return username;
    }

    public String getAvatarBase64() {
        String avatarBase64 = this.avatarBase64;
        if (avatarBase64 == null) {
            avatarBase64 = Converters.convert(getAvatarBitmap());
        }
        return avatarBase64;
    }

    public Bitmap getAvatarBitmap() {
        Bitmap avatarBitmap = this.avatarBitmap;
        if (avatarBitmap == null) {
            if (avatarBase64 != null) {
                avatarBitmap = Converters.convert(avatarBase64);
            } else {
                Avatar avatar = getAvatar();
                if (avatar != null) {
                    avatarBitmap = avatar.getImage();
                }
            }
        }
        return avatarBitmap;
    }

    public Date getLoginTime() {
        return loginTime;
    }
}
