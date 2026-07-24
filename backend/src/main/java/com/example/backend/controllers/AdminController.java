package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.AdminRepo;
import com.example.backend.db.dao.FacilityRepo;
import com.example.backend.db.dao.TrainerRepo;
import com.example.backend.models.Admin;
import com.example.backend.models.Facility;
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

    @GetMapping("/getPendingRequests")
    public List<User> getPendingRequests() {
        return new AdminRepo().getPendingRequests();
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

    @PostMapping("/updateUser")
    public Message updateUser(@RequestBody User u) {
        return new AdminRepo().updateUser(u);
    }

    @PostMapping("/deleteUser")
    public Message deleteUser(@RequestBody int userId) {
        return new AdminRepo().deleteUser(userId);
    }
    
    @GetMapping("/getPendingFacilities")
    public List<Facility> getPendingFacilities() {
        return new FacilityRepo().getPendingFacilities();
    }

    @PostMapping("/acceptFacilityRequest")
    public Message acceptFacilityRequest(@RequestBody int facilityId) {
        return new AdminRepo().acceptFacilityRequest(facilityId);
    }

    @PostMapping("/denyFacilityRequest")
    public Message denyFacilityRequest(@RequestBody int facilityId) {
        return new AdminRepo().denyFacilityRequest(facilityId);
    }

    @PostMapping("/deactivateTrainer")
    public Message deactivateTrainer(@RequestBody int trainerId) {
        return new TrainerRepo().deactivateTrainer(trainerId);
    }
    
}
