package com.example.backend.controllers;

// import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.dao.UserRepo;
import com.example.backend.models.Athlete;
import com.example.backend.models.Message;
import com.example.backend.models.Worker;
import com.example.backend.models.helpers.ChangePasswordObject;
import com.example.backend.models.helpers.FavoriteSportsObject;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    // ovo gore ne radi zbog hesiranja lozinke..
    @PostMapping("/loginAthlete")
    public ResponseEntity<Athlete> loginAthlete(@RequestBody Athlete a) {
        Athlete result = new UserRepo().loginAthlete(a);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/loginWorker")
    public ResponseEntity<Worker> loginWorker(@RequestBody Worker w) {
        Worker result = new UserRepo().loginWorker(w);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/registerAthlete")
    public int registerAthlete(@RequestBody Athlete a) {
        return new UserRepo().registerAthlete(a);
    }
    
    @PostMapping("/registerWorker")
    public int registerWorker(@RequestBody Worker w) {
        return new UserRepo().registerWorker(w);
    }

    // @GetMapping("/hashTest")
    // public String hashTest() {
    //     return BCrypt.hashpw("Test1234!", BCrypt.gensalt());
    // }

    @GetMapping("/getAthlete/{username}")
    public Athlete getAthlete(@PathVariable String username) {
        return new UserRepo().getAthlete(username);
    }

    @GetMapping("/getAthleteById/{id}")
    public Athlete getAthleteById(@PathVariable int id) {
        return new UserRepo().getAthleteById(id);
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

    // UserController.java
    @PostMapping("/uploadProfileImage")
    public String uploadProfileImage(@RequestParam String username,
                                    @RequestParam MultipartFile image) {
        return new UserRepo().uploadProfileImage(username, image);
    }
}
