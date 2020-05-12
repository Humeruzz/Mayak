package com.mymess.mayak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mymess.mayak.pojo.User;
import com.mymess.mayak.pojo.UserInfo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mymess.mayak.JsonPlaceHolderApi.STATUS_NAME;

public class SettingsActivity extends AppCompatActivity{

    TextView first;
    TextView last;
    ImageView profileImage;

    TextView phone;
    TextView nick;
    TextView birthdate;
    TextView email;
    TextView pass;
    
    TextView update;
    Toolbar toolbar;

    JsonPlaceHolderApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        first = findViewById(R.id.settings_first);
        last = findViewById(R.id.settings_last);
        profileImage = findViewById(R.id.settings_profileImage);
        phone = findViewById(R.id.settings_phone_person);
        nick = findViewById(R.id.settings_nick_person);
        birthdate = findViewById(R.id.settings_birthdate_person);
        email = findViewById(R.id.settings_email_person);
        pass = findViewById(R.id.settings_pass_person);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://95.25.239.19:8080/Mayak_war_exploded/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JsonPlaceHolderApi.class);
        
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
        if(ok!="") {
            String[] temp = ok.split("\n");
           // Toast.makeText(this,temp[0],Toast.LENGTH_LONG).show();
            User user = new User(Integer.parseInt(temp[0]),temp[1],temp[2]);
            return user;
        }
        return new User();
    }

    public void LoadData() {
        getUserInfo();
        getProfileImage();
    }

    private void getUserInfo(){
        final User user = loadCash();
        if(user.getUserId() != null) {
            Call<UserInfo> call = api.getUserInfo(user.getUserId().toString());

            call.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(SettingsActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UserInfo userinfo = response.body();
                    first.setText(userinfo.getFirstName());
                    last.setText(userinfo.getLastName());
                    phone.setText(userinfo.getPhoneNumber());
                    nick.setText(userinfo.getNickName());
                    Date date = new Date(userinfo.getDateOfBirth());
                    birthdate.setText(date.getDate()+"."+(date.getMonth()+1)+"."+date.getYear());
                    email.setText(user.getEmail());
                    pass.setText(user.getPassword());
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    Toast.makeText(SettingsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getProfileImage() {
        final User user = loadCash();
        if(user.getUserId() != null) {
            Call<ResponseBody> call = api.getProfileImage(user.getUserId().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()) {
                    //    Toast.makeText(SettingsActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    profileImage.setImageBitmap(bmp);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                 //   Toast.makeText(SettingsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LoadData();
    }
}
