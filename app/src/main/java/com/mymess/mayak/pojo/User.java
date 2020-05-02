package com.mymess.mayak.pojo;

public class User {
    private int userId;
    private String email;
    private String password;

    public User() {
        this.userId = 0;
        this.email = null;
        this.password = null;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(int userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (!email.equals(user.email)) return false;
        return password.equals(user.password);
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
