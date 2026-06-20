package com.example.backend.controllers;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.UserRepo;
import com.example.backend.models.Athlete;
import com.example.backend.models.Message;
import com.example.backend.models.Reservation;
import com.example.backend.models.Sport;
import com.example.backend.models.Worker;
import com.example.backend.models.helpers.ChangePasswordObject;
import com.example.backend.models.helpers.FavoriteSportsObject;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @PostMapping("/loginAthlete")
    public Athlete loginAthlete(@RequestBody Athlete a) {
        return new UserRepo().loginAthlete(a);
    }

    @PostMapping("/loginWorker")
    public Worker loginWorker(@RequestBody Worker w) {
        return new UserRepo().loginWorker(w);
    }

    @PostMapping("/registerAthlete")
    public int registerAthlete(@RequestBody Athlete a) {
        return new UserRepo().registerAthlete(a);
    }
    
    @PostMapping("/registerWorker")
    public int registerWorker(@RequestBody Worker w) {
        return new UserRepo().registerWorker(w);
    }

    @GetMapping("/hashTest")
    public String hashTest() {
        return BCrypt.hashpw("Test1234!", BCrypt.gensalt());
    }

    @GetMapping("/getAthlete/{username}")
    public Athlete getAthlete(@PathVariable String username) {
        return new UserRepo().getAthlete(username);
    }

    @GetMapping("/getWorker/{username}")
    public Worker getWorker(@PathVariable String username) {
        return new UserRepo().getWorker(username);
    }

    @PostMapping("/updateAthlete")
    public Message updateAthlete(@RequestBody Athlete a) {
        return new UserRepo().updateAthlete(a);
    }

    @PostMapping("/updateFavoriteSports")
    public Message updateFavoriteSports(@RequestBody FavoriteSportsObject obj) {
        return new UserRepo().updateFavoriteSports(obj);
    }
    
    @PostMapping("/changePassword")
    public Message changePassword(@RequestBody ChangePasswordObject obj) {
        return new UserRepo().changePassword(obj);
    }
    
    @GetMapping("/getReservations/{athleteId}")
    public List<Reservation> getReservations(@PathVariable int athleteId) {
        return new UserRepo().getReservations(athleteId);
    }

    @PostMapping("/cancelReservation")
    public Message cancelReservation(@RequestBody int id) {
        return new UserRepo().cancelReservation(id);
    }

    @GetMapping("/getAllSports")
    public List<Sport> getAllSports() {
        return new UserRepo().getAllSports();
    }
}
