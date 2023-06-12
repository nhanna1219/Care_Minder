package com.example.careminder.Activity.Login_Signup;

public class User {
    String name;
    String email;
    // check first login
    boolean firstLogin = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }
    public boolean checkFirstLogin() {
        return firstLogin;
    }
    String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
//        this.firstLogin = true;
    }

    public User() {
    }
}
