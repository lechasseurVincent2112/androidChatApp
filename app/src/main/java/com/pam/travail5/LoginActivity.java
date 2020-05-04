package com.pam.travail5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.pam.travail5.model.Session;

import java.io.File;
import java.io.IOException;

public class LoginActivity extends ChatActivity {

    public static final String USERNAME = "username";
    public static final int REQUEST_CODE = 1;
    private EditText login;
    private ProgressBar progressBar;
    private ImageView avatar;
    private File avatarFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        avatar = findViewById(R.id.avatar);
        login = findViewById(R.id.nickname);
        progressBar = findViewById(R.id.progressBar);
        if (getUserManager().getLocalUser().getUsername() != null){
            login.setText(getUserManager().getLocalUser().getUsername(), TextView.BufferType.EDITABLE);
        }
        if (getUserManager().getLocalUser().getAvatarBitmap() != null){
            avatar.setImageBitmap(getUserManager().getLocalUser().getAvatarBitmap());
        }
        login.setOnEditorActionListener((v, actionId, event) -> {
            if (!hasUsername()) return false;
            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                login.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                getUserManager().getLocalUser().setUsername(getUsername());
                getSocket().connect();
                return true;
            }
            return false;
        });

        getChatApplication().getUuid();
    }

    public String getUsername() {
        return login.getText().toString();
    }

    private boolean hasUsername() {
        return !"".equals(getUsername().trim());
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
        login.setEnabled(true);
    }

    @Override
    public void onConnected(String url) {
        super.onConnected(url);
        getConnectionManager().login();
    }

    @Override
    public void onLoggedIn(Session session) {
        super.onLoggedIn(session);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            File outPutDir = getCacheDir();
            avatarFile = new File(outPutDir, "avatar");
            Uri avatarUri = FileProvider.getUriForFile(this, "avatarAuth", avatarFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, avatarUri);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            try {
                ExifInterface exif = new ExifInterface(avatarFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                float rotate = 0.f;
                switch (orientation){
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }

                Bitmap avatarImage = BitmapFactory.decodeFile(avatarFile.getAbsolutePath());
                int w = avatarImage.getWidth();
                int h = avatarImage.getHeight();

                float max = 256;
                float ratio = max / Math.max(w, h);

                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                matrix.postScale(ratio, ratio);

                avatarImage = Bitmap.createBitmap(avatarImage, 0, 0, w, h ,matrix, true);

                avatar.setImageBitmap(avatarImage);
                getUserManager().getLocalUser().setAvatar(avatarImage);

               // avatarFile.delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
