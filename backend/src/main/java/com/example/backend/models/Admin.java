package com.example.backend.models;

public class Admin extends User{
    
    public Admin(int id, String username, String password, String firstname, String lastname,
                String email, String phone, String profileImage) {
        super(id, username, password, firstname, lastname, email, phone, profileImage, "/", "ADMIN");
    }
   
}
