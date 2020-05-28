package com.example.tabadvertsbusiness.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {


    @SerializedName("status")
    private boolean status;

    @SerializedName("role")
    @Expose
    private Role role;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("message")
    @Expose
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(boolean status, Role role, String token, String message) {
        this.status = status;
        this.role = role;
        this.token = token;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
