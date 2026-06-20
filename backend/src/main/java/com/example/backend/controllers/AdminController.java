package com.example.backend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.AdminRepo;
import com.example.backend.models.Admin;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
    
    @PostMapping("/loginAdmin")
    public Admin login(@RequestBody Admin a) {
        return new AdminRepo().loginAdmin(a);
    }
}
