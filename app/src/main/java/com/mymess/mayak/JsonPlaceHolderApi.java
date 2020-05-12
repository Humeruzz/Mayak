package com.mymess.mayak;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.mymess.mayak.pojo.FullMessage;
import com.mymess.mayak.pojo.ProfileImage;
import com.mymess.mayak.pojo.User;
import com.mymess.mayak.pojo.UserInfo;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    static final String STATUS_NAME = "statusID.txt";

    @POST("user")
    Call<User> createUser(@Body User user);

    @POST("user/info")
    Call<UserInfo> createUserInfo(@Body UserInfo userinfo);

    @GET("user/{id}")
    Call<User> getUser(@Path("id") String user_id);

    @GET("user")
    Call<User> getUser(@Query("email") String email, @Query("password") String pass);

    @GET("user/info/{id}")
    Call<UserInfo> getUserInfo(@Path("id") String user_id);

    @GET("user/{id}/dialogs")
    Call<List<UserInfo>> getDialogs(@Path("id") String user_id);

    @GET("message/user/{mainUserId}/{anotherUserId}/messages/timestamp/before/{timestamp}")
    Call<List<FullMessage>> getMessagesBefore(@Path("mainUserId") String mainUserId,
                                              @Path("anotherUserId") String anotherUserId,
                                              @Path("timestamp") long timestamp,
                                              @Query("limit") String limit,
                                              @Query("offset") String offset);

    @GET("user/info/nickname/{nickname}")
    Call<UserInfo> getUserByNick(@Path("nickname") String string);

    @POST("message")
    Call<FullMessage> createFullMessage(@Body FullMessage fullMessage);

    @GET("user/{id}/profile/image")
    Call<ResponseBody> getProfileImage(@Path("id") String user_id);

    @PUT("user/email/{email}/password")
    Call<Void> updateUserPass(@Path("email") String email,
                              @Query("oldPassword") String oldPassword,
                              @Query("newPassword") String newPassword);

    @PUT("user/info/{id}")
    Call<Void> updateUserInfo(@Path("id") String user_id, @Body UserInfo info);

    @Multipart
    @POST("user/{id}/profile/image")
    Call<Void> createProfileImage(@Path("id") String user_id, @Part("filename")RequestBody name, @Part MultipartBody.Part image);
}
