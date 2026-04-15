package com.inventory.server.dto.auth;

public class LoginResponseDTO {
    private String jwt;
    private Long userId;
    private String username;
    private String role;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String jwt, Long userId, String username, String role) {
        this.jwt = jwt;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
