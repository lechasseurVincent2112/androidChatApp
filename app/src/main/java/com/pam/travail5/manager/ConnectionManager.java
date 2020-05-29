package com.pam.travail5.manager;

import android.os.Looper;

import com.pam.travail5.ChatApplication;
import com.pam.travail5.model.Message;
import com.pam.travail5.model.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import java.util.Date;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static io.socket.client.Socket.EVENT_CONNECT;
import static io.socket.client.Socket.EVENT_DISCONNECT;

public class ConnectionManager {

    public static final String LOGIN = "login";
    public static final String NEW_MESSAGE = "new message";
    public static final String ADD_USER = "add user";

    public static final String AVATAR = "avatar";
    public static final String USERID = "userid";
    public static final String USERNAME = "username";
    public static final String LOGTIME = "logtime";
    public static final String USERS = "users";
    public static final String MESSAGE = "message";
    public static final String USER_JOINED = "user joined";

    public interface Listener {
        void onLoggedIn(Session session);

        void onDisconnected();

        void onConnected(String url);

        void onUserJoined(Session session);

        void onMessage(Message message);

    }

    private Socket socket;
    private final Handler runOnUiThread;
    private Listener listener;
    private final UserManager userManager;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Socket getSocket() {
        return socket;
    }

    public ConnectionManager(Socket socket, UserManager userManager) {
        this.socket = socket;
        runOnUiThread = new Handler(Looper.getMainLooper());
        this.userManager = userManager;
    }

    protected void on(String event, Emitter.Listener listener) {
        getSocket().on(event, args -> runOnUiThread(() -> listener.call(args)));
    }

    private void runOnUiThread(Runnable runnable) {
        runOnUiThread.post(runnable);
    }

    protected void onMessage(Message message) {
        System.out.println("message recevied");
    }

    public void pause() {
        getSocket().off();
    }

    public void onLoggedIn(JSONObject data) {
        System.out.println("LOGGEDIN");
        Session session = getUserManager().getLocalUser().newSession();
        listener.onLoggedIn(session);
        try {
            JSONArray users = data.getJSONArray(USERS);
            for (int i = 0; i < users.length(); i++) {
                onUserJoined(users.getJSONObject(i));

            }
        } catch (JSONException e) {

        }
    }

    public Message sendMessage(String message, String tag) {
        getSocket().emit(NEW_MESSAGE, message);
        return getUserManager().getLocalUser().newMessage(message, tag);
    }

    private void onUserJoined(JSONObject data) {
        try {
            String username = data.getString(USERNAME);
            String userid = data.getString(USERID);
            String avatarBase64 = null;
            Date loginTime = null;
            if (data.has(AVATAR)) {
                avatarBase64 = data.getString(AVATAR);
                if (avatarBase64.length() >= 200e3) {
                    avatarBase64 = null;
                }
            }
            if (data.has(LOGTIME)) {
                loginTime = new Date(data.getLong(LOGTIME));
            }
            Session session = getUserManager().getRemoteUser(userid).setAvatar(avatarBase64).setUsername(username).setLoginTime(loginTime).newSession();
            listener.onUserJoined(session);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void onDisconnected() {
        System.out.println("LOGGED OUT");
        listener.onDisconnected();
    }

    protected void onConnected(String url) {
        System.out.println("CONNECTED TO " + url);
        listener.onConnected(url);
    }

    public void login() {
        JSONObject jsonObject = new JSONObject();
        String username = getUserManager().getLocalUser().getUsername();
        String avatar = getUserManager().getLocalUser().getAvatarBase64();
        String uuid = getUserManager().getLocalUser().getUuid();
        try {
            jsonObject.put(AVATAR, avatar);
            jsonObject.put(USERID, uuid);
            jsonObject.put(USERNAME, username);
            getSocket().emit(ADD_USER, jsonObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private UserManager getUserManager() {
        return this.userManager;
    }

    protected void onRecupMess() {
        System.out.println("Récupération des messages dans la base de données");
    }

    private void onMessageReceived(JSONObject data) {
        String userid = null;
        try {
            String content = data.getString(MESSAGE);
            userid = data.getString(USERID);
            Message message = getUserManager().getRemoteUser(userid).newMessage(content);
            listener.onMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void resume() {

        on(EVENT_CONNECT, args -> onConnected(ChatApplication.URL));

        on(EVENT_DISCONNECT, args -> this.onDisconnected());

        on(LOGIN, args -> this.onLoggedIn((JSONObject) args[0]));

        on(NEW_MESSAGE, args -> {
            this.onMessageReceived((JSONObject) args[0]);
        });

        on(USER_JOINED, args -> this.onUserJoined((JSONObject)args[0]));
    }

}
