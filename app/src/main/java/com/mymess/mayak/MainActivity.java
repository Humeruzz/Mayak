package com.mymess.mayak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.mymess.mayak.Adapters.MessageAdapter;
import com.mymess.mayak.pojo.UserInfo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private static final String STATUS_NAME = "status.txt";

    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolStart);
        setSupportActionBar(toolbar);


        initRecyclerView();
        loadMess();
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
}
