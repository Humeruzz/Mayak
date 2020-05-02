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

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, IDataUse {

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
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        LoadData();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataControl()) {
                    SaveData();
                    finish();
                }
            }
        });

    }

    @Override
    public void LoadData() {
        //loadData;
    }

    @Override
    public void SaveData() {
        //saveData;
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
}