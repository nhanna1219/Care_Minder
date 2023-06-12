package com.example.careminder.Activity.Login_Signup;

public class User {
    private String name;
    private String firstLogin;
    private String email;
    private String password;

    public User() {
        // Constructor mặc định để Firestore có thể tạo đối tượng từ dữ liệu truy vấn
    }

    public User(String name, String firstLogin, String email, String password) {
        this.name = name;
        this.firstLogin = firstLogin;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
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

    public boolean checkFirstLogin() {
        return firstLogin.equals("true");
    }

}
