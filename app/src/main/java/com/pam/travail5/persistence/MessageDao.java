package com.pam.travail5.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pam.travail5.model.Message;
import com.pam.travail5.model.MessageSession;
import com.pam.travail5.model.Session;

import java.util.List;

@Dao
public abstract class MessageDao {

    public Message insert(Session session, int type, String data){
        long id = insert(new Message(data, type, session.getId()));
        return queryById(id);
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(Message message);

    @Query("SELECT * FROM messages where id = :id")
    public abstract MessageSession queryByIdWithSession(long id);

    @Query("SELECT * FROM messages where id = :id")
    public abstract Message queryById(long id);

    @Query(value = "SELECT * from messages order by time")
    public abstract List<MessageSession> queryForAll();

    @Query(value = "SELECT * from messages where message like '%' + :tag + '%'")
    public abstract List<MessageSession> queryByTag(String tag);

}
