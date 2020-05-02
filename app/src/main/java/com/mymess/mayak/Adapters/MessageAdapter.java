package com.mymess.mayak.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mymess.mayak.R;
import com.mymess.mayak.pojo.UserInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<UserInfo> messList = new ArrayList<>();
    private OnChatClickListener onChatClickListener;
    private Context parent;

    public MessageAdapter(OnChatClickListener onChatClickListener, Context parent) {
        this.onChatClickListener = onChatClickListener;
        this.parent = parent;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        private ImageView userIm;
        private TextView userName;
        private TextView userText;
        private TextView userTime;

        public MessageViewHolder (View itemView){
            super(itemView);
            userIm = itemView.findViewById(R.id.user_mess_im);
            userName = itemView.findViewById(R.id.user_mess_name);
            userText = itemView.findViewById(R.id.user_mess_text);
            userTime = itemView.findViewById(R.id.user_mess_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positionIndex = getAdapterPosition();
                    UserInfo mess = messList.get(positionIndex);
                    onChatClickListener.onChatClick(mess);
                }
            });
        }

        public void bind(UserInfo message){
            userName.setText(message.getNickName()+"("+message.getFirstName()+")");
            userText.setText("null");
            userTime.setText(message.getCreationTimestamp().toString());

            //Picasso.get().load(message.getUser().getImageUrl()).into(userIm);
        }
    }

    public void setItems(Collection<UserInfo> mess){
        messList.addAll(mess);
        notifyDataSetChanged();
    }

    public void clearItems(){
        messList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_view, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(messList.get(position));
    }


    @Override
    public int getItemCount() {
        return messList.size();
    }

    public interface OnChatClickListener {
        void onChatClick(UserInfo messageChat);
    }
}
