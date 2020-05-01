package com.pam.travail5.persistence;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.pam.travail5.model.Avatar;

@Dao
public abstract class AvatarDao {

    public Avatar queryOrCreate(Avatar avatar) {
        if (avatar.getId() >= 1) {
            return avatar;
        }
        long id = insert(avatar);
        if (id != -1) {
            avatar = queryForId(id);
        } else {
            avatar = queryForData(avatar.getImage());
        }
        return avatar;
    }

    @TypeConverters({Converters.class})
    @Query("SELECT * FROM AVATARS WHERE image = :image")
    public abstract Avatar queryForData(Bitmap image);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(Avatar avatar);

    @Query("Select * from avatars where id = :id")
    public abstract Avatar queryForId(long id);

    @Query("Select avatars.* from avatars, sessions, users " +
            "where sessions.avatarId = avatars.id " +
            "and sessions.avatarId != 1 " +
            "and sessions.userId = users.id " +
            "and users.uuid = :uuid " +
            "order by sessions.login desc limit 1")
    public abstract Avatar queryForLast(String uuid);
}
