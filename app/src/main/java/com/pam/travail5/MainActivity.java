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

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

public class MainActivity extends ChatActivity {

    public static final String MESSAGE_DB_DB = "message-db.db";
    private RecyclerView list;
    private EditText editText;
    private String username;
    private MessageAdapter adapter;
    private List<MessageSession> messages;
    private ChatDatabase db;
    private ExpandableLayout expandableLayout;
    private EditText inputTextEmoji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFullScreen();
        list = findViewById(R.id.messages);
        editText = findViewById(R.id.inputText);
        expandableLayout = findViewById(R.id.expandable_layout);
        inputTextEmoji = findViewById(R.id.inputTextEmoji);
        username = getIntent().getStringExtra(LoginActivity.USERNAME);
        db = Room.databaseBuilder(this, ChatDatabase.class, MESSAGE_DB_DB).allowMainThreadQueries().build();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);
        messages = getDatabaseManager().getAllMessages();
        adapter = new MessageAdapter(messages);
        list.setAdapter(adapter);
    }

    private void setFullScreen() {
        View root = getWindow().getDecorView();
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
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
        Message msg = getConnectionManager().sendMessage(message, "tagTest");
        add(msg);
        editText.clearFocus();
        editText.setText("");

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
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

    public void searchEmoji() {
        if (inputTextEmoji.getText().toString().trim() == "") {
            messages = getDatabaseManager().getAllMessages();
        } else {
            messages = getDatabaseManager().getMessageByTag(inputTextEmoji.getText().toString().trim());
        }
        adapter.notifyDataSetChanged();
        list.scrollToPosition(messages.size() - 1);
    }

    public void animateSearchBar(View view) {
        if (expandableLayout.isExpanded()) {
            expandableLayout.collapse();
            inputTextEmoji.clearFocus();
            inputTextEmoji.setText("");
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            expandableLayout.expand();
        }
    }
}
