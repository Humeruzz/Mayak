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
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.mymess.mayak.Adapters.MessageAdapter;
import com.mymess.mayak.pojo.UserInfo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String STATUS_NAME = "status.txt";

    private DrawerLayout drawer;
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.home_drawer);
        toolbar = findViewById(R.id.toolStart);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView nav = findViewById(R.id.home_nav);
        nav.setNavigationItemSelectedListener(this);


        initRecyclerView();
        loadMess();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadMess() {
        Collection<UserInfo> mess = getMessages();
        messageAdapter.setItems(mess);
    }

    private void initRecyclerView() {
        messageRecyclerView = findViewById(R.id.mess_view);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MessageAdapter.OnChatClickListener onChatClickListener = new MessageAdapter.OnChatClickListener() {
            @Override
            public void onChatClick(UserInfo mess) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("User_id", "2");
                startActivity(intent);
            }
        };
        messageAdapter = new MessageAdapter(onChatClickListener, this);
        messageRecyclerView.setAdapter(messageAdapter);
    }

    private Collection<UserInfo> getMessages() {
        Collection<UserInfo> temp = new ArrayList<UserInfo>();
        UserInfo ufo = new UserInfo();
        ufo.setFirstName("GG");
        ufo.setNickName("gg");
        ufo.setCreationTimestamp(new Timestamp(2000, 10, 14, 4, 5, 6, 0));
        temp.add(ufo);
        return temp;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //  saveCash();
    }

    private void saveCash() {
        String text = "nook";
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(STATUS_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
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
        if(item.getItemId() == R.id.menu_home_addmess){
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Добавить пользователя");
            alert.setMessage("Напишите Никнейм пользователя, которого хотите добавить");
            final EditText text = new EditText(this);
            alert.setView(text);
            alert.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //send data
                    //recive data
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    intent.putExtra("User_id","11");
                    startActivity(intent);
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
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Load Data
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_drawer_home_settings:
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_drawer_home_about:
                final AlertDialog.Builder alert= new AlertDialog.Builder(this);
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
        }
        return true;
    }

    public void onProfileClick(View view){
        Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(intent);
    }
}
