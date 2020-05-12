package com.mymess.mayak.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class UserInfo {
    @SerializedName("user_id")
    private Integer userId;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("nickname")
    private String nickName;
    @SerializedName("date_of_birth")
    private long dateOfBirth;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("creation_timestamp")
    private long creationTimestamp;

    public UserInfo() {
    }

    public UserInfo(int userId, String firstName, String lastName, String nickName, long dateOfBirth, String phoneNumber, long creationTimestamp) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.creationTimestamp = creationTimestamp;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (dateOfBirth != userInfo.dateOfBirth) return false;
        if (creationTimestamp != userInfo.creationTimestamp) return false;
        if (!userId.equals(userInfo.userId)) return false;
        if (firstName != null ? !firstName.equals(userInfo.firstName) : userInfo.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(userInfo.lastName) : userInfo.lastName != null)
            return false;
        if (nickName != null ? !nickName.equals(userInfo.nickName) : userInfo.nickName != null)
            return false;
        return phoneNumber != null ? phoneNumber.equals(userInfo.phoneNumber) : userInfo.phoneNumber == null;
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (nickName != null ? nickName.hashCode() : 0);
        result = 31 * result + (int) (dateOfBirth ^ (dateOfBirth >>> 32));
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (int) (creationTimestamp ^ (creationTimestamp >>> 32));
        return result;
    }
}
