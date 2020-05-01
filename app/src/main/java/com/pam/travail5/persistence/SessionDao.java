package com.pam.travail5.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pam.travail5.model.Session;

@Dao
public abstract class SessionDao {

    @Insert
    public abstract long insert(Session session);

    @Query("Select * from sessions where id = :id")
    public abstract Session queryForId(long id);

    @Query("Select sessions.* from sessions " +
            "inner Join users " +
            "on sessions.userId = users.id " +
            "WHERE users.uuid = :userUuid ORDER BY sessions.login desc limit 1")
    public abstract Session queryForLast(String userUuid);
}
