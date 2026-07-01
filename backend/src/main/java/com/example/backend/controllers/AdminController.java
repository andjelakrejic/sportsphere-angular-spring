package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.AdminRepo;
import com.example.backend.models.Admin;
import com.example.backend.models.Message;
import com.example.backend.models.Sport;
import com.example.backend.models.User;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
    
    @PostMapping("/loginAdmin")
    public Admin login(@RequestBody Admin a) {
        return new AdminRepo().loginAdmin(a);
    }

    @PostMapping("/addSport")
    public Message addSport(@RequestBody Sport s) {
        return new AdminRepo().addSport(s);
    }

    @PostMapping("/acceptRequest")
    public Message acceptRequest(@RequestBody int userId) {        
        return new AdminRepo().acceptRequest(userId);
    }

    @PostMapping("/denyRequest")
    public Message denyRequest(@RequestBody int userId) {        
        return new AdminRepo().denyRequest(userId);
    }

    @GetMapping("/viewAllAccounts")
    public List<User> viewAllAccounts() {
        return new AdminRepo().viewAllAccounts();
    }
    
    
}
