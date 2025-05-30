package com.community.issuereporting.dto;

import com.community.issuereporting.entity.Role;

public class UserBasicDTO {
    private Long id;
    private String name;
    private String email;
    private String username;
    private Role role;

    // Constructors
    public UserBasicDTO() {}

    public UserBasicDTO(Long id, String name, String email, String username, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}