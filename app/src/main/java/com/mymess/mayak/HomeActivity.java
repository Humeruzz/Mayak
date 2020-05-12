package com.mymess.mayak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mymess.mayak.Adapters.MessageAdapter;
import com.mymess.mayak.pojo.ProfileImage;
import com.mymess.mayak.pojo.User;
import com.mymess.mayak.pojo.UserInfo;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mymess.mayak.JsonPlaceHolderApi.STATUS_NAME;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView first;
    private TextView last;
    private TextView phone;
    private ImageView profileImage;
    private DrawerLayout drawer;
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;
    private Toolbar toolbar;

    JsonPlaceHolderApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.home_drawer);
        toolbar = findViewById(R.id.toolStart);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView nav = findViewById(R.id.home_nav);
        nav.setNavigationItemSelectedListener(this);

        View hView = nav.getHeaderView(0);
        first = hView.findViewById(R.id.nav_header_home_first);
        last = hView.findViewById(R.id.nav_header_home_last);
        phone = hView.findViewById(R.id.nav_header_home_phone);
        profileImage = hView.findViewById(R.id.nav_header_home_profileImage);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://95.25.239.19:8080/Mayak_war_exploded/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JsonPlaceHolderApi.class);

        loadData();
    }

    private void loadData(){
        getUserInfo();
        getProfileImage();
        loadMess();
        initRecyclerView();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadMess() {
        getDialogs();
    }

    private void initRecyclerView() {
        messageRecyclerView = findViewById(R.id.mess_view);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MessageAdapter.OnChatClickListener onChatClickListener = new MessageAdapter.OnChatClickListener() {
            @Override
            public void onChatClick(UserInfo mess) {
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                if(mess.getUserId() != null) {
                    intent.putExtra("User_id", mess.getUserId().toString());
                }
                startActivity(intent);
            }
        };
        messageAdapter = new MessageAdapter(onChatClickListener, this,loadCash());
        messageRecyclerView.setAdapter(messageAdapter);
    }


    private void saveCash() {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(STATUS_NAME, MODE_PRIVATE);
            fos.write("".getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_home_addmess) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Добавить пользователя");
            alert.setMessage("Напишите Никнейм пользователя, которого хотите добавить");
            final EditText text = new EditText(this);
            alert.setView(text);
            alert.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getUserByNick(text.getText().toString().trim());
                }
            });
            alert.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alert.show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_drawer_home_settings:
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_drawer_home_about:
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("О нас");
                alert.setMessage("Команда разработки данного приложения:\nГруппа ИКБО-16-18 МИРЭА\nСеменихин А.О. (фронт)\nКасьяненко К.В (бэк)");
                alert.setNeutralButton("Спасибо", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
                break;
            case R.id.menu_drawer_home_exit:
                saveCash();
                Intent intent1 = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
        return true;
    }

    public void onProfileClick(View view) {
        User user = loadCash();
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        if (user.getUserId() != null) {
            intent.putExtra("User_id", user.getUserId().toString());
        }
        startActivity(intent);
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

    private void getUserInfo() {
        final User user = loadCash();
        if (user.getUserId() != null) {
            Call<UserInfo> call = api.getUserInfo(user.getUserId().toString());

            call.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(HomeActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UserInfo userinfo = response.body();
                    first.setText(userinfo.getFirstName());
                    last.setText(userinfo.getLastName());
                    phone.setText(userinfo.getPhoneNumber());
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getDialogs() {
        final User user = loadCash();
        if (user.getUserId() != null) {
            Call<List<UserInfo>> call = api.getDialogs(user.getUserId().toString());

            call.enqueue(new Callback<List<UserInfo>>() {
                @Override
                public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(HomeActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<UserInfo> userInfos = response.body();
                    messageAdapter.setItems(userInfos);
                }

                @Override
                public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getUserByNick(String nickname){
        Call<UserInfo> call = api.getUserByNick(nickname);

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserInfo userInfo = response.body();
                if(userInfo.getUserId() != null) {
                    Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                    intent.putExtra("User_id", userInfo.getUserId().toString());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
               // Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProfileImage(){
        final User user = loadCash();
        if (user.getUserId() != null) {
            Call<ResponseBody> call = api.getProfileImage(user.getUserId().toString());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()) {
                      //  Toast.makeText(HomeActivity.this, "Code: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    profileImage.setImageBitmap(bmp);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                   // Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
