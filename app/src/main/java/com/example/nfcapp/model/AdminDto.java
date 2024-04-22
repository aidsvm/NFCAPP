package com.example.nfcapp.model;

public class AdminDto {
    private Long adminId;
    private String username;
    private String password;

    public AdminDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // getters and setters
    public Long getAdminId() {
        return adminId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

