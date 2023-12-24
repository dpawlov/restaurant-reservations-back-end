package com.service.dto;

import javax.validation.constraints.NotNull;

public class UserDto {

    @NotNull(message = "Id cannot be empty!")
    private Long id;

    @NotNull(message = "Username cannot be empty!")
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
