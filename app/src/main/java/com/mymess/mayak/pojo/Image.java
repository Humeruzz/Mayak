package com.mymess.mayak.pojo;

import java.util.Arrays;

public class Image {

    String type;
    byte[] source;
    private int messageId;
    private int imageId;


    public Image() {
    }

    public Image(String type, byte[] source) {
        this.type = type;
        this.source = source;
    }
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getSource() {
        return source;
    }

    public String getSourceStr(){
        return source.toString();
    }

    public void setSource(byte[] source) {
        this.source = source;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (messageId != image.messageId) return false;
        if (imageId != image.imageId) return false;
        if (type != null ? !type.equals(image.type) : image.type != null) return false;
        return Arrays.equals(source, image.source);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(source);
        result = 31 * result + messageId;
        result = 31 * result + imageId;
        return result;
    }
}
