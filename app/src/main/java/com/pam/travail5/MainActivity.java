package com.pam.travail5;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.pam.travail5.model.Message;
import com.pam.travail5.model.MessageSession;
import com.pam.travail5.persistence.ChatDatabase;

import java.util.List;

public class MainActivity extends ChatActivity {

    private RecyclerView list;
    private EditText editText;
    private String username;
    private MessageAdapter adapter;
    private List<MessageSession> messages;
    private ChatDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.messages);
        editText = findViewById(R.id.inputText);

        username = getIntent().getStringExtra(LoginActivity.USERNAME);
        db = Room.databaseBuilder(this, ChatDatabase.class, "message-db.db").allowMainThreadQueries().build();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);
        messages = getDatabaseManager().getAllMessages();
        adapter = new MessageAdapter(messages);
        list.setAdapter(adapter);
//        recupMsg();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getSocket().close();
        getChatDatabase().close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getSocket().connect();
    }

    @Override
    public void onConnected(String url) {
        super.onConnected(url);
        getConnectionManager().login();
    }

    public void send(View view) {
        String message = editText.getText().toString().trim();
        if (message.equals("")) return;
        Message msg = getConnectionManager().sendMessage(message);
        add(msg);
        editText.clearFocus();
        editText.setText("");

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        add(message);
    }

    private void add(Message message) {
        messages.add(getDatabaseManager().getMessageDetails(message.getId()));
        adapter.notifyDataSetChanged();
        list.scrollToPosition(messages.size() - 1);
    }
}
