package com.mymess.mayak.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class ProfileImage {
    private Integer userId;
    @SerializedName("binary image source")
    private byte[] source;

    public ProfileImage() {
    }

    public ProfileImage(byte[] source) {
        this.source = source;
    }

    public ProfileImage(Integer userId, byte[] source) {
        this.userId = userId;
        this.source = source;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public byte[] getSource() {
        return source;
    }

    public void setSource(byte[] source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfileImage that = (ProfileImage) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return Arrays.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(source);
        return result;
    }


}
