package com.mymess.mayak.pojo;

public class FullMessage {
    Image image;
    Message message;
    Text text;

    public FullMessage() {
    }

    public FullMessage(Message message,Image image, Text text) {
        this.image = image;
        this.message = message;
        this.text = text;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
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

        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return text != null ? text.equals(that.text) : that.text == null;
    }

    @Override
    public int hashCode() {
        int result = image != null ? image.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
