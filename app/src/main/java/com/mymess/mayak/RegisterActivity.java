package com.mymess.mayak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String STATUS_NAME = "status.txt";

    EditText email;
    EditText pass;
    EditText passAgain;
    EditText first;
    EditText last;
    EditText nick;
    PhoneInputLayout phone;
    Button register;
    EditText birthdate;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.register_toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        email = findViewById(R.id.register_email_field);
        pass = findViewById(R.id.register_pass_field);
        passAgain = findViewById(R.id.register_pass_doub_field);
        first = findViewById(R.id.register_first_field);
        last = findViewById(R.id.register_last_field);
        nick = findViewById(R.id.register_nick_field);
        phone = findViewById(R.id.register_phone_field);
        birthdate = findViewById(R.id.register_birth_field);
        register = findViewById(R.id.register_register_bt);
        phone.setDefaultCountry("RU");
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataControl()) {
                    saveCash();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
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
        String temp = dayOfMonth + "." + month + "." + year;
        birthdate.setText(temp);
    }

    public boolean dataControl() {
        if (email.getText().toString().matches("([A-z0-9_.-]{1,})@([A-z0-9_.-]{1,}).([A-z]{2,8})")) {
            if ((pass.getText().toString().equals(passAgain.getText().toString())) && (pass.getText().toString().matches("([A-z0-9_.-]{8,})"))) {
                if ((first.getText().toString().matches("([A-zА-я_.-]{1,})")) && (last.getText().toString().matches("([A-zА-я_.-]{1,})"))) {
                    if (nick.getText().toString().matches("([A-zА-я0-9_.-]{4,})")) {
                        if (phone.isValid()) {
                            if (birthdate.getText().toString().matches("([0-9]{1,2}).([0-9]{1,2}).([0-9]{4})")) {
                                //send data
                                //recive data
                                //if(data = "ok")
                                return true;
                                //else
                                //return false
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

    private void saveCash() {
        String text = "ok";
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
}
