package com.mymess.mayak.pojo;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class Message {
    private boolean checked;
    @SerializedName("message_id")
    private int messageId;
    private int from;
    private int to;
    private long timestamp;

    public Message(boolean checked, int from, int to) {
        this.checked = checked;
        this.from = from;
        this.to = to;
    }

    public Message(boolean checked, int messageId, int from, int to, long timestamp) {
        this.checked = checked;
        this.messageId = messageId;
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (messageId != message.messageId) return false;
        if (from != message.from) return false;
        if (to != message.to) return false;
        if (timestamp != message.timestamp) return false;
        return checked == message.checked;
    }

    @Override
    public int hashCode() {
        int result = messageId;
        result = 31 * result + from;
        result = 31 * result + to;
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (checked ? 1 : 0);
        return result;
    }
}
