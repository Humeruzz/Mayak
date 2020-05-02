package com.mymess.mayak.pojo;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private int from;
    private int to;
    private Timestamp timestamp;
    private boolean checked;

    public Message(int from, int to, Timestamp timestamp) {
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
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
        if (checked != message.checked) return false;
        return timestamp.equals(message.timestamp);
    }

    @Override
    public int hashCode() {
        int result = messageId;
        result = 31 * result + from;
        result = 31 * result + to;
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + (checked ? 1 : 0);
        return result;
    }
}
