package com.example.backend.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.MediaType;
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
import com.example.backend.models.Sport;
import com.example.backend.models.helpers.ChangePasswordObject;
import com.example.backend.models.helpers.FavoriteSportsObject;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @PostMapping("/uploadProfileImage")
    public String uploadProfileImage(@RequestParam String username,
                                    @RequestParam MultipartFile image) {
        return new UserRepo().uploadProfileImage(username, image);
    }

    // Register
    @PostMapping(value = "/registerAthlete", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerAthlete(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam(required = false) String favoriteSports,
            @RequestParam(required = false) MultipartFile image
    ) {
        UserRepo repo = new UserRepo();

        if (repo.usernameExists(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message("Username already taken."));
        }
        if (repo.emailExists(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message("Email already registered."));
        }

        Athlete a = new Athlete();
        a.setUsername(username);
        a.setPassword(password);
        a.setFirstname(firstname);
        a.setLastname(lastname);
        a.setEmail(email);
        a.setPhone(phone);

        if (image != null && !image.isEmpty()) {
            try {
                String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Files.write(Paths.get("images/" + filename), image.getBytes());
                a.setProfileImage(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int userId = repo.registerAthlete(a);

        if (userId > 0 && favoriteSports != null && !favoriteSports.isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Sport[] sports = mapper.readValue(favoriteSports, Sport[].class);
                for (Sport s : sports) {
                    repo.addFavoriteSport(userId, s.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (userId > 0) {
            return ResponseEntity.ok(userId);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(false,"Registration failed."));
    }

    @GetMapping("/getFavoriteSports/{id}")
    public List<Sport> getAthlete(@PathVariable int id) {
        return new UserRepo().getFavoriteSports(id);
    }
    
}
