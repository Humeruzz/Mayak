package com.mymess.mayak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;
import com.mymess.mayak.pojo.ProfileImage;
import com.mymess.mayak.pojo.User;
import com.mymess.mayak.pojo.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.sql.Date;
import java.util.concurrent.TimeUnit;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String STATUS_NAME = "statusID.txt";
    static final int GALLERY_REQUEST = 1;

    EditText email;
    EditText pass;
    EditText passAgain;
    EditText first;
    EditText last;
    EditText nick;
    PhoneInputLayout phone;
    Button register;
    Button imageButton;
    EditText birthdate;
    Toolbar toolbar;
    Date birth;
    Bitmap bitmap = null;

    JsonPlaceHolderApi api;

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

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://95.25.239.19:8080/Mayak_war_exploded/").addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(JsonPlaceHolderApi.class);


        email = findViewById(R.id.register_email_field);
        pass = findViewById(R.id.register_pass_field);
        passAgain = findViewById(R.id.register_pass_doub_field);
        first = findViewById(R.id.register_first_field);
        last = findViewById(R.id.register_last_field);
        nick = findViewById(R.id.register_nick_field);
        phone = findViewById(R.id.register_phone_field);
        birthdate = findViewById(R.id.register_birth_field);
        register = findViewById(R.id.register_register_bt);
        imageButton = findViewById(R.id.register_image_button);
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
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                TimeUnit.MILLISECONDS.sleep(1000);
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                }
            }
        });

        /*imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfAlreadyhavePermission()) {
                    ActivityCompat.requestPermissions(RegisterActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                }
            }
        });*/
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
        birth = new Date(year, month, dayOfMonth);
        birthdate.setText(dayOfMonth + "." + (month + 1) + "." + year);
    }

    public boolean dataControl() {
        if (email.getText().toString().matches("([A-z0-9_.-]{1,})@([A-z0-9_.-]{1,}).([A-z]{2,8})")) {
            if ((pass.getText().toString().equals(passAgain.getText().toString())) && (pass.getText().toString().matches("([A-z0-9_.-]{8,})"))) {
                if ((first.getText().toString().matches("([A-zА-я_.-]{1,})")) && (last.getText().toString().matches("([A-zА-я_.-]{1,})"))) {
                    if (nick.getText().toString().matches("([A-zА-я0-9_.-]{4,})")) {
                        if (phone.isValid()) {
                            if (birthdate.getText().toString().matches("([0-9]{1,2}).([0-9]{1,2}).([0-9]{4})")) {
                                createUser();
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
           // e.printStackTrace();
        } catch (IOException e) {
           // e.printStackTrace();
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

    private void createUser() {
        final User user = new User(email.getText().toString(), pass.getText().toString());

        Call<User> call = api.createUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                User userResponse = response.body();
                user.setUserId(userResponse.getUserId());
                saveCash(user);
                createUserInfo(user);
               // createProfileImage(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUserInfo(User user) {
        final UserInfo userInfo = new UserInfo(user.getUserId(), first.getText().toString(), last.getText().toString(), nick.getText().toString(),
                birth.getTime(), phone.getEditText().getText().toString(), new Timestamp(System.currentTimeMillis()).getTime());

        Call<UserInfo> call = api.createUserInfo(userInfo);

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {

                if (!response.isSuccessful()) {
                   // Toast.makeText(RegisterActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
               // Toast.makeText(RegisterActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void createProfileImage(User user) {
        if (user.getUserId() != null && bitmap != null) {
            String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap.recycle();*/

           // RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"),byteArray);
           /* MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("filename", "Photo", requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), imgname);
            Call<Void> call = api.createProfileImage(user.getUserId().toString(), filename,fileToUpload);
            call.enqueue(new Callback<Void>() {
                @Override*/
               /* public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(RegisterActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    bitmap = decodeSampledBitmapFromResource(filePath,1000,1000);

                    imageButton.setText("Готово");
                }
        }
    }*/

    /*public static Bitmap decodeSampledBitmapFromResource(String path,
                                                         int reqWidth, int reqHeight) {

        // Читаем с inJustDecodeBounds=true для определения размеров
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Вычисляем inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Читаем с использованием inSampleSize коэффициента
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }*/

    /*public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Реальные размеры изображения
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Вычисляем наибольший inSampleSize, который будет кратным двум
            // и оставит полученные размеры больше, чем требуемые
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }*/

    /*private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

                } else {
                    Toast.makeText(this, "Please give your permission.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }*/
}
