package com.mymess.mayak.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.mymess.mayak.JsonPlaceHolderApi;
import com.mymess.mayak.R;
import com.mymess.mayak.pojo.FullMessage;
import com.mymess.mayak.pojo.Image;
import com.mymess.mayak.pojo.Message;
import com.mymess.mayak.pojo.Text;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<FullMessage> messList = new ArrayList<>();
    private String userId;
    private Retrofit retrofit;
    private JsonPlaceHolderApi api;

    public ChatAdapter(String user_id) {
        userId = user_id;
        retrofit = new Retrofit.Builder()
                .baseUrl("http://95.25.239.19:8080/Mayak_war_exploded/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JsonPlaceHolderApi.class);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(messList.get(messList.size() - 1 - position));
    }

    @Override
    public int getItemCount() {
        return messList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
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

        public void bind(FullMessage messageText) {
            if (userCheck(String.valueOf(messageText.getMessage().getFrom()))) {
                layoutR.setVisibility(View.GONE);
                layoutL.setVisibility(View.VISIBLE);
                chatTextL.setText(messageText.getText().getText());
                chatImL.setVisibility(View.GONE);

                //In Progress
                /*if (messageText.getImage() != null) {
                    for (Image image : messageText.getImage()) {
                        System.out.println(image + " Image");
                        String messageImageUrl = String.valueOf(image.getSource());
                        Transformation transformation = new RoundedTransformationBuilder().cornerRadiusDp(30).build();
                        Picasso.get().load(image.getSourceStr()).fit().transform(transformation).into(chatImL);
                        chatImL.setVisibility(messageImageUrl != null ? View.VISIBLE : View.GONE);

                    }
                } else {

                }*/
            } else {
                layoutL.setVisibility(View.GONE);
                layoutR.setVisibility(View.VISIBLE);
                chatTextR.setText(messageText.getText().getText());
                chatImR.setVisibility(View.GONE);

                //In Progress
                /*if (messageText.getImage() != null) {
                    for (Image image : messageText.getImage()) {
                        System.out.println(image + " Image");
                        String messageImageUrl = String.valueOf(image.getSource());
                        Transformation transformation = new RoundedTransformationBuilder().cornerRadiusDp(30).build();
                        Picasso.get().load(image.getSourceStr()).fit().transform(transformation).into(chatImR);
                        chatImR.setVisibility(messageImageUrl != null ? View.VISIBLE : View.GONE);

                    }
                } else {

                }*/
            }
        }

        private boolean userCheck(String id) {
            if (userId.equals(id.trim())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void setItems(Collection<FullMessage> texts) {
        messList.addAll(texts);
        notifyDataSetChanged();
    }

    public void clearItems() {
        messList.clear();
        notifyDataSetChanged();
    }

}
