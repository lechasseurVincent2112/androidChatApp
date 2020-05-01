package com.pam.travail5;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pam.travail5.model.MessageSession;

import java.text.DateFormat;

public class MessageHolder extends RecyclerView.ViewHolder {
    private TextView message;
    private ImageView avatar;
    private TextView date;
    public MessageHolder(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
        date = itemView.findViewById(R.id.dateMsg);
        avatar = itemView.findViewById(R.id.avatar);
    }

    public void update(MessageSession message){
        this.message.setText(message.getMessage().getMessage());
        DateFormat formater = DateFormat.getDateInstance(DateFormat.MEDIUM);
        this.date.setText(formater.format(message.getMessage().getTime()));
        if (message.getDetails().getAvatar() != null && message.getDetails().getAvatar().getImage() != null){
            avatar.setVisibility(View.VISIBLE);
            avatar.setImageBitmap(message.getDetails().getAvatar().getImage());
        } else {
            avatar.setVisibility(View.INVISIBLE);
        }
    }
}
