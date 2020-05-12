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

import com.mymess.mayak.pojo.ProfileImage;
import com.mymess.mayak.pojo.User;
import com.mymess.mayak.pojo.UserInfo;
import com.squareup.picasso.Picasso;

import java.sql.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private String user_id = "";
    private Toolbar toolbar;
    private ImageView profileImage;
    private TextView profileName;

    private TextView phone;
    private TextView birthdate;
    private TextView nickname;

    JsonPlaceHolderApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("User_id");

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
        phone = findViewById(R.id.profile_phone_info);
        birthdate = findViewById(R.id.profile_birthdate_info);
        nickname = findViewById(R.id.profile_nickname_info);
        profileImage = findViewById(R.id.profile_img);
        profileName = findViewById(R.id.profile_name);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://95.25.239.19:8080/Mayak_war_exploded/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JsonPlaceHolderApi.class);

        getProfileImage();
        getUserInfo();

    }


    private void getUserInfo() {
        Call<UserInfo> call = api.getUserInfo(user_id);

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                UserInfo userInfo = response.body();
                if (userInfo != null) {
                    profileName.setText(userInfo.getFirstName() + " " + userInfo.getLastName());
                    phone.setText(userInfo.getPhoneNumber());
                    nickname.setText(userInfo.getNickName());
                    Date date = new Date(userInfo.getDateOfBirth());
                    birthdate.setText(date.getDate() + "." + (date.getMonth()+1) + "." + date.getYear());
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProfileImage() {

        Call<ResponseBody> call = api.getProfileImage(user_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                  //  Toast.makeText(ProfileActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                profileImage.setImageBitmap(bmp);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
              //  Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
