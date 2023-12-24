package com.service.dto;

import javax.validation.constraints.NotNull;

public class UserCreateDto {

    @NotNull(message = "Username cannot be empty!")
    private String username;
    @NotNull(message = "Password cannot be empty!")
    private String password;
    @NotNull(message = "Password confirmation cannot be empty!")
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
