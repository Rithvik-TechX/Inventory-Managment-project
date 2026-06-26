package com.inventory.model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
    private Timestamp createdAt;

    public User() {}

    public User(String name, String email, String password,
                String phoneNumber, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public int getId()                     { return id; }
    public void setId(int id)              { this.id = id; }
    public String getName()                { return name; }
    public void setName(String name)       { this.name = name; }
    public String getEmail()               { return email; }
    public void setEmail(String email)     { this.email = email; }
    public String getPassword()            { return password; }
    public void setPassword(String pwd)    { this.password = pwd; }
    public String getPhoneNumber()         { return phoneNumber; }
    public void setPhoneNumber(String ph)  { this.phoneNumber = ph; }
    public String getRole()                { return role; }
    public void setRole(String role)       { this.role = role; }
    public Timestamp getCreatedAt()        { return createdAt; }
    public void setCreatedAt(Timestamp t)  { this.createdAt = t; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', role='" + role + "'}";
    }
}