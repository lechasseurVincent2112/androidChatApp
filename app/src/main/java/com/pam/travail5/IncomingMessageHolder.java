package com.pam.travail5;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pam.travail5.model.MessageSession;

public class IncomingMessageHolder extends MessageHolder {

    private final TextView user;

    public IncomingMessageHolder(@NonNull View itemView) {
        super(itemView);
        user = itemView.findViewById(R.id.user);
    }

    @Override
    public void update(MessageSession message) {
        super.update(message);
        user.setText(message.getDetails().getSession().getUsername());
    }

}
