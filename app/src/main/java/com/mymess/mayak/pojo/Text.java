package com.mymess.mayak.pojo;

import com.google.gson.annotations.SerializedName;



public class Text {
    private int textId;
    private int messageId;
    String text;

    public Text() {
    }

    public Text(String text) {
        this.text = text;
    }

    public Text(int textId, int messageId, String text) {
        this.textId = textId;
        this.messageId = messageId;
        this.text = text;
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int textId) {
        this.textId = textId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Text text1 = (Text) o;

        if (textId != text1.textId) return false;
        if (messageId != text1.messageId) return false;
        return text != null ? text.equals(text1.text) : text1.text == null;
    }

    @Override
    public int hashCode() {
        int result = textId;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + messageId;
        return result;
    }
}
