package com.mymess.mayak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;
import com.mymess.mayak.pojo.User;
import com.mymess.mayak.pojo.UserInfo;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mymess.mayak.JsonPlaceHolderApi.STATUS_NAME;

public class UpdateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText email;
    EditText pass;
    EditText passAgain;
    EditText first;
    EditText last;
    EditText nick;
    PhoneInputLayout phone;
    Button update;
    EditText birthdate;
    Toolbar toolbar;
    JsonPlaceHolderApi api;
    Date birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        toolbar = findViewById(R.id.update_toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        email = findViewById(R.id.update_email_field);
        pass = findViewById(R.id.update_pass_field);
        passAgain = findViewById(R.id.update_pass_doub_field);
        first = findViewById(R.id.update_first_field);
        last = findViewById(R.id.update_last_field);
        nick = findViewById(R.id.update_nick_field);
        phone = findViewById(R.id.update_phone_field);
        birthdate = findViewById(R.id.update_birth_field);
        update = findViewById(R.id.update_update_bt);
        phone.setDefaultCountry("RU");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://95.25.239.19:8080/Mayak_war_exploded/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JsonPlaceHolderApi.class);

        getUserInfo();

        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataControl()) {
                    finish();
                }
            }
        });

    }

    public void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        birth = new Date(year,month,dayOfMonth+1);
        birthdate.setText(dayOfMonth+"."+(month+1)+"."+year);
    }

    public boolean dataControl() {
        if (email.getText().toString().matches("([A-z0-9_.-]{1,})@([A-z0-9_.-]{1,}).([A-z]{2,8})")) {
            if ((pass.getText().toString().equals(passAgain.getText().toString())) && (pass.getText().toString().matches("([A-z0-9_.-]{8,})"))) {
                if ((first.getText().toString().matches("([A-zА-я_.-]{1,})")) && (last.getText().toString().matches("([A-zА-я_.-]{1,})"))) {
                    if (nick.getText().toString().matches("([A-zА-я0-9_.-]{4,})")) {
                        if (phone.isValid()) {
                            if (birthdate.getText().toString().matches("([0-9]{1,2}).([0-9]{1,2}).([0-9]{4})")) {
                                updatePass();
                                updateUserInfo();
                                return true;
                            } else {
                                Toast.makeText(this, "Incorrect format of Birth date", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Incorrect format of Phone number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Incorrect format of Nick name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Incorrect format of First or Last name", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Incorrect format of Password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Incorrect format of Email", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getUserInfo(){
        User user = loadCash();
        if(user.getUserId() != null){
            Call<UserInfo> call = api.getUserInfo(user.getUserId().toString());

            call.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(UpdateActivity.this,"Code: "+ response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UserInfo info = response.body();
                    email.setText(user.getEmail());
                    pass.setText(user.getPassword());
                    passAgain.setText(user.getPassword());
                    first.setText(info.getFirstName());
                    last.setText(info.getLastName());
                    nick.setText(info.getNickName());
                    phone.getEditText().setText(info.getPhoneNumber());
                    Date date = new Date(info.getDateOfBirth());
                    birthdate.setText(date.getDate()+"."+(date.getMonth()+1)+"."+date.getYear());
                    birth = date;
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    Toast.makeText(UpdateActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updatePass(){
        User user = loadCash();
        if(user.getUserId() != null && user.getPassword() != pass.getText().toString()){
            Call<Void> call = api.updateUserPass(user.getEmail(),user.getPassword(),pass.getText().toString());

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(UpdateActivity.this,"Code: "+ response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    User newUser = new User(user.getUserId(),user.getEmail(),pass.getText().toString());
                    saveCash(newUser);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(UpdateActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateUserInfo(){
        User user = loadCash();
        if(user.getUserId()!=null){
            UserInfo info = new UserInfo(user.getUserId(),first.getText().toString(),last.getText().toString(),nick.getText().toString(),
                    birth.getTime(),phone.getEditText().getText().toString(),new Timestamp(System.currentTimeMillis()).getTime());
            Call<Void> call = api.updateUserInfo(user.getUserId().toString(), info);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(UpdateActivity.this,"Code: "+ response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(UpdateActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
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
        if (ok != "") {
            String[] temp = ok.split("\n");
            User user = new User(Integer.parseInt(temp[0]), temp[1], temp[2]);
            return user;
        }
        return new User();
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
}