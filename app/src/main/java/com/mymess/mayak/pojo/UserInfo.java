package com.mymess.mayak.pojo;

import java.sql.Timestamp;
import java.util.Calendar;

public class UserInfo {
    private int userId;
    private String firstName;
    private String lastName;
    private String nickName;
    private Calendar dateOfBirth;
    private String phoneNumber;
    private Timestamp creationTimestamp;

    public UserInfo() {
    }

    public UserInfo(int userId, String firstName, String lastName, String nickName, Calendar dateOfBirth, String phoneNumber, Timestamp creationTimestamp) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.creationTimestamp = creationTimestamp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (userId != userInfo.userId) return false;
        if (!firstName.equals(userInfo.firstName)) return false;
        if (!lastName.equals(userInfo.lastName)) return false;
        if (nickName != null ? !nickName.equals(userInfo.nickName) : userInfo.nickName != null)
            return false;
        if (!dateOfBirth.equals(userInfo.dateOfBirth)) return false;
        if (!phoneNumber.equals(userInfo.phoneNumber)) return false;
        return creationTimestamp.equals(userInfo.creationTimestamp);
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + (nickName != null ? nickName.hashCode() : 0);
        result = 31 * result + dateOfBirth.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + creationTimestamp.hashCode();
        return result;
    }
}
