package com.mymess.mayak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.mymess.mayak.Adapters.ChatAdapter;
import com.mymess.mayak.pojo.Message;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

public class ChatActivity extends AppCompatActivity implements IDataUse {

    public String user_id = "user_id";
    public String image = "https://de.sott.net/image/s8/174380/full/putin_ba_rv3.jpg";

    private ImageButton emojiB;
    private ImageButton sendB;
    private ImageView profileIm;
    private TextView profileName;
    private EditText sendText;
    private Toolbar toolbar;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Load DATA

        Transformation transformation = new RoundedTransformationBuilder().cornerRadiusDp(30).build();
        Picasso.get().load(image).fit().transform(transformation).into(profileIm);

        emojiB = findViewById(R.id.emoji_b);
        sendB = findViewById(R.id.send_b);
        sendText = findViewById(R.id.send_text);



        initRecyclerView();
        loadChat();
    }

    private void initRecyclerView(){
        chatRecyclerView = findViewById(R.id.chat_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(manager);

        chatAdapter = new ChatAdapter(user_id);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    private void loadChat(){
        Collection<Message> texts = getTexts();
        chatAdapter.setItems(texts);
    }

    private Collection<Message> getTexts() {
        Collection<Message> temp = new ArrayList<Message>();
        Message ufo = new Message(1,2,new Timestamp(2,2,2,2,2,2,2));
        temp.add(ufo);
        return temp;
    }

    public void ProfileOpen(View view) {
        Intent intent = new Intent(ChatActivity.this,ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void LoadData() {

    }

    @Override
    public void SaveData() {

    }
}
