package com.mymess.mayak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mymess.mayak.pojo.User;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mymess.mayak.JsonPlaceHolderApi.STATUS_NAME;

public class LoginActivity extends AppCompatActivity {

    private static final String STATUS_NAME = "statusID.txt";

    EditText email;
    EditText pass;
    TextView register;
    Button login;
    JsonPlaceHolderApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_login_field);
        pass = findViewById(R.id.login_pass_field);
        register = findViewById(R.id.login_register);
        login = findViewById(R.id.login_login_bt);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://95.25.239.19:8080/Mayak_war_exploded/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JsonPlaceHolderApi.class);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser();
            }
        });

        checkUser();
    }


    private void saveCash(User user) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(STATUS_NAME, MODE_PRIVATE);
            fos.write(user.getUserId().toString().getBytes());
            fos.write("\n".getBytes());
            fos.write(user.getEmail().getBytes());
            fos.write("\n".getBytes());
            fos.write(user.getPassword().getBytes());
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
            User user = new User(Integer.parseInt(temp[0]),temp[1],temp[2]);
            return user;
        }
        return new User();
    }

    private void checkUser() {
        final User userOut = loadCash();
        if(userOut.getUserId() != null) {
            Call<User> call = api.getUser(userOut.getUserId().toString());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Server Off", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    User user = response.body();
                    if ((userOut.getEmail().equals(user.getEmail())) && (userOut.getPassword().equals(user.getPassword()))) {
                        mainActivityStart();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getUser() {
        User user = new User(email.getText().toString(), pass.getText().toString());

        Call<User> call = api.getUser(email.getText().toString(), pass.getText().toString());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Wrong Email or Pass\nOr Server Off", Toast.LENGTH_SHORT).show();
                    return;
                }
                User tempUser = response.body();
                if (tempUser.getUserId() != null) {
                    user.setUserId(tempUser.getUserId());
                    saveCash(user);
                    checkUser();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mainActivityStart() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
