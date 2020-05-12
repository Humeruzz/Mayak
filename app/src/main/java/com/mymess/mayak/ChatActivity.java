package com.mymess.mayak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.mymess.mayak.Adapters.ChatAdapter;
import com.mymess.mayak.pojo.FullMessage;
import com.mymess.mayak.pojo.Image;
import com.mymess.mayak.pojo.Message;
import com.mymess.mayak.pojo.Text;
import com.mymess.mayak.pojo.User;
import com.mymess.mayak.pojo.UserInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mymess.mayak.JsonPlaceHolderApi.STATUS_NAME;

public class ChatActivity extends AppCompatActivity {

    public String user_id = "";

    private ImageButton emojiB;
    private ImageButton sendB;
    private ImageView profileIm;
    private TextView profileName;
    private EditText sendText;
    private Toolbar toolbar;

    private List<FullMessage> fullMessages = new ArrayList<>();
    JsonPlaceHolderApi api;

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("User_id");

        profileIm = findViewById(R.id.im);
        profileName = findViewById(R.id.name);
        toolbar = findViewById(R.id.toolChat);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://95.25.239.19:8080/Mayak_war_exploded/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JsonPlaceHolderApi.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        emojiB = findViewById(R.id.emoji_b);
        sendB = findViewById(R.id.send_b);
        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMessage();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                            ChatActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadChat();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
        sendText = findViewById(R.id.send_text);
        loadChat();
        getUserInfo(user_id);
        getProfileImage(user_id);

        initRecyclerView();

    }

    private void initRecyclerView() {
        chatRecyclerView = findViewById(R.id.chat_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(manager);

        chatAdapter = new ChatAdapter(user_id);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    private void loadChat() {
        fullMessages.clear();
        getMessages(0);
    }

    public void ProfileOpen(View view) {
        Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
        intent.putExtra("User_id", user_id);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadChat();
    }

    private void getMessages(int offset) {
        User user = loadCash();
        if (user.getUserId() != null) {
            Timestamp times = new Timestamp(System.currentTimeMillis());
            times.setMinutes(times.getMinutes() + 20);
            long time = times.getTime();
            Call<List<FullMessage>> call = api.getMessagesBefore(user.getUserId().toString(), user_id
                    , time, "20", String.valueOf(offset));

            call.enqueue(new Callback<List<FullMessage>>() {
                @Override
                public void onResponse(Call<List<FullMessage>> call, Response<List<FullMessage>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(ChatActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (offset == 0) {
                        List<FullMessage> mess = response.body();
                        fullMessages.clear();
                        chatAdapter.clearItems();
                        fullMessages.addAll(mess);
                        chatAdapter.setItems(fullMessages);
                    } else {
                        if (response.body().isEmpty()) {
                            chatAdapter.clearItems();
                            chatAdapter.setItems(fullMessages);
                        } else {
                            List<FullMessage> mess = response.body();
                            fullMessages.addAll(mess);
                            getMessages(offset + 20);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<FullMessage>> call, Throwable t) {
                    Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getUserInfo(String user_id) {
        Call<UserInfo> call = api.getUserInfo(user_id);

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ChatActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                UserInfo userInfo = response.body();
                if (userInfo.getUserId() != null) {
                    profileName.setText(userInfo.getFirstName() + " " + userInfo.getLastName());
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createMessage() {
        User user = loadCash();
        if (user.getUserId() != null) {
            FullMessage message = new FullMessage(
                    new Message(false, user.getUserId(), Integer.parseInt(user_id)),
                    new Text(sendText.getText().toString().trim()),
                    new Image[]{});
            Call<FullMessage> call = api.createFullMessage(message);

            call.enqueue(new Callback<FullMessage>() {
                @Override
                public void onResponse(Call<FullMessage> call, Response<FullMessage> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(ChatActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendText.setText("");
                }

                @Override
                public void onFailure(Call<FullMessage> call, Throwable t) {
                    Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getProfileImage(String user_id) {
        Call<ResponseBody> call = api.getProfileImage(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                 //   Toast.makeText(ChatActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                profileIm.setImageBitmap(bmp);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
               // Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private User loadCash() {
        FileInputStream fis = null;
        String ok = "";

        try {
            fis = openFileInput(STATUS_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            ok = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (ok != "") {
            String[] temp = ok.split("\n");
            User user = new User(Integer.parseInt(temp[0]), temp[1], temp[2]);
            return user;
        }
        return new User();
    }
}
