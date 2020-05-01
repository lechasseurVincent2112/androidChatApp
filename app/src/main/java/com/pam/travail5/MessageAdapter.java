package com.pam.travail5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pam.travail5.model.Message;
import com.pam.travail5.model.MessageSession;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {

    private List<MessageSession> messages;


    public MessageAdapter(List<MessageSession> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getMessage().getType();
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MessageHolder row = null;
        int layout = 0;
        switch (viewType) {
            case Message.TYPE_INCOMING:
                layout = R.layout.message_in;
                break;
            case Message.TYPE_OUTGOING:
                layout = R.layout.message_out;
                break;
        }
        View root = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        switch (viewType) {
            case Message.TYPE_INCOMING:
                row = new IncomingMessageHolder(root);
                break;
            case Message.TYPE_OUTGOING:
                row = new MessageHolder(root);
                break;
        }

        return row;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.update(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
