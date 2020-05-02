package com.mymess.mayak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    public String image;
    private ImageView profileImage;
    private TextView profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        image = intent.getStringExtra("Image");
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolProfile);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        profileImage = findViewById(R.id.profile_img);
        profileName = findViewById(R.id.profile_name);
        Picasso.get().load(image).fit().into(profileImage);


    }
}
