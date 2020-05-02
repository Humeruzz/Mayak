package com.mymess.mayak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements IDataUse{
    
    TextView phone;
    TextView nick;
    TextView birthdate;
    TextView email;
    TextView pass;
    
    TextView update;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        phone = findViewById(R.id.settings_phone_person);
        nick = findViewById(R.id.settings_nick_person);
        birthdate = findViewById(R.id.settings_birthdate_person);
        email = findViewById(R.id.settings_email_person);
        pass = findViewById(R.id.settings_pass_person);
        
        LoadData();
        
        update = findViewById(R.id.settings_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,UpdateActivity.class);
                startActivity(intent);
            }
        });
        
        toolbar = findViewById(R.id.settings_toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void LoadData() {

    }

    @Override
    public void SaveData() {
    }
}
