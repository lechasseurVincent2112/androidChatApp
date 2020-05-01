package com.pam.travail5.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pam.travail5.model.User;

@Dao
public abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(User user);

    public User queryOrCreate(String uuid){
        insert(new User(uuid));
        return queryForUuid(uuid);
    }

    @Query("Select * from users where uuid = :uuid")
    public abstract User queryForUuid(String uuid);

    @Query("Select * from users where id = :id")
    public abstract User queryForId(long id);
}
