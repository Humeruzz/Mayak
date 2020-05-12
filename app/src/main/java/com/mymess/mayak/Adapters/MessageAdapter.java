package com.mymess.mayak.Adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mymess.mayak.HomeActivity;
import com.mymess.mayak.JsonPlaceHolderApi;
import com.mymess.mayak.R;
import com.mymess.mayak.pojo.FullMessage;
import com.mymess.mayak.pojo.User;
import com.mymess.mayak.pojo.UserInfo;

import java.io.Console;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<UserInfo> messList = new ArrayList<>();
    private OnChatClickListener onChatClickListener;
    private Context parent;
    private JsonPlaceHolderApi api;
    private Retrofit retrofit;
    private User user;


    public MessageAdapter(OnChatClickListener onChatClickListener, Context parent, User user) {
        this.onChatClickListener = onChatClickListener;
        this.parent = parent;
        this.user = user;

        retrofit = new Retrofit.Builder()
                .baseUrl("http://95.25.239.19:8080/Mayak_war_exploded/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JsonPlaceHolderApi.class);
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private ImageView userIm;
        private TextView userName;
        private TextView userText;
        private TextView userTime;

        public MessageViewHolder(View itemView) {
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

        public void bind(UserInfo message) {
            userName.setText(message.getFirstName() + " " + message.getLastName());
            getMessages(user, message);
            getProfileImage(message);
        }

        private void getMessages(User user1, UserInfo user2) {
            if ((user1.getUserId() != null) && (user2.getUserId() != null)) {
                long time = new Timestamp(System.currentTimeMillis()).getTime();
                Call<List<FullMessage>> call = api.getMessagesBefore(
                        user1.getUserId().toString(),
                        user2.getUserId().toString(),
                        time,
                        "1", "0");
                call.enqueue(new Callback<List<FullMessage>>() {
                    @Override
                    public void onResponse(Call<List<FullMessage>> call, Response<List<FullMessage>> response) {
                        if (!response.isSuccessful()) {
                            System.out.println("Code " + response.code());
                            return;
                        }
                        List<FullMessage> fullMessages = response.body();
                        FullMessage fullMessage = fullMessages.get(0);
                        userText.setText(fullMessage.getText().getText());
                        Timestamp time = new Timestamp(fullMessage.getMessage().getTimestamp());
                        userTime.setText(time.getHours() + ":" + time.getMinutes());

                    }

                    @Override
                    public void onFailure(Call<List<FullMessage>> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            }
        }

        private void getProfileImage(UserInfo info) {
            if (info.getUserId() != null) {
                Call<ResponseBody> call = api.getProfileImage(info.getUserId().toString());

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            //Toast.makeText(HomeActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
                            userIm.setImageResource(R.drawable.mayak);
                            return;
                        }
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        userIm.setImageBitmap(bmp);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public void setItems(Collection<UserInfo> mess) {
        messList.addAll(mess);
        notifyDataSetChanged();
    }

    public void clearItems() {
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
