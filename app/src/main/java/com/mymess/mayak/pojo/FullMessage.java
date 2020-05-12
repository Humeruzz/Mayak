package com.mymess.mayak.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class FullMessage {
    @SerializedName("messageInfo")
    Message message;
    @SerializedName("text")
    Text text;
    @SerializedName("images")
    Image[] image;

    public FullMessage(){}

    public FullMessage(Message message, Text text, Image[] image) {
        this.image = image;
        this.message = message;
        this.text = text;
    }

    public Image[] getImage() {
        return image;
    }

    public void setImage(Image[] image) {
        this.image = image;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FullMessage that = (FullMessage) o;

        if (!message.equals(that.message)) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }
}
