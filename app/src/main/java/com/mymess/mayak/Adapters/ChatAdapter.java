package com.mymess.mayak.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mymess.mayak.R;
import com.mymess.mayak.pojo.FullMessage;
import com.mymess.mayak.pojo.Image;
import com.mymess.mayak.pojo.Message;
import com.mymess.mayak.pojo.Text;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Message> messList = new ArrayList<>();
    private String userId;

    public ChatAdapter(String user_id) {
        userId = user_id;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message,parent,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(messList.get(position));
    }

    @Override
    public int getItemCount() {
        return messList.size();
    }

    class  ChatViewHolder extends RecyclerView.ViewHolder{
        private ImageView chatImR;
        private TextView chatTextR;
        private ImageView chatImL;
        private TextView chatTextL;
        private ConstraintLayout layoutR;
        private ConstraintLayout layoutL;

        public ChatViewHolder(View itemView) {
            super(itemView);
            chatImR = itemView.findViewById(R.id.message_image_r);
            chatTextR = itemView.findViewById(R.id.message_text_r);
            layoutR = itemView.findViewById(R.id.message_layout_right);
            chatImL = itemView.findViewById(R.id.message_image_l);
            chatTextL = itemView.findViewById(R.id.message_text_l);
            layoutL = itemView.findViewById(R.id.message_layout_left);
        }

        public void bind(Message messageText){
            FullMessage full = getData(messageText);
            if (usercheck(String.valueOf(messageText.getTo()))) {
                chatTextL.setText(full.getText().getText());
                layoutR.setVisibility(View.GONE);
                layoutL.setVisibility(View.VISIBLE);
               // String messageImageUrl = full.getImage().getSourceStr();
               // Transformation transformation = new RoundedTransformationBuilder().cornerRadiusDp(30).build();
               // Picasso.get().load(full.getImage().getSourceStr()).fit().transform(transformation).into(chatImL);
               // chatImL.setVisibility(messageImageUrl != null ? View.VISIBLE : View.GONE);
            } else {
                chatTextR.setText(full.getText().getText());
                layoutL.setVisibility(View.GONE);
                layoutR.setVisibility(View.VISIBLE);
                //String messageImageUrl = full.getImage().getSourceStr();
                //Transformation transformation = new RoundedTransformationBuilder().cornerRadiusDp(30).build();
               // Picasso.get().load(full.getImage().getSourceStr()).fit().transform(transformation).into(chatImR);
               // chatImR.setVisibility(messageImageUrl != null ? View.VISIBLE : View.GONE);
            }
        }
        private boolean usercheck(String id){
            if(!userId.trim().equals(id.trim())){
                return true;
            } else {
                return false;
            }
        }
    }

    public void setItems(Collection<Message> texts){
        messList.addAll(texts);
        notifyDataSetChanged();
    }

    public void clearItems(){
        messList.clear();
        notifyDataSetChanged();
    }

    public FullMessage getData(Message mess){
        //Выгрузка инфы
        return new FullMessage(new Message(1,2,new Timestamp(2,2,2,2,2,2,2)),new Image(),new Text("Hello"));
    }
}
