package com.example.backend.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.example.backend.db.dao.CourtRepo;
import com.example.backend.db.dao.FacilityRepo;
import com.example.backend.db.dao.UserRepo;
import com.example.backend.db.dao.WorkerRepo;
import com.example.backend.models.Court;
import com.example.backend.models.Facility;
import com.example.backend.models.Message;
import com.example.backend.models.Worker;
import com.example.backend.models.helpers.ChangePasswordObject;
import com.example.backend.models.helpers.FacilityWorkerOption;


@RestController
@RequestMapping("/workers")
@CrossOrigin(origins = "http://localhost:4200")
public class WorkerController {

    @PostMapping("/loginWorker")
    public ResponseEntity<Worker> loginWorker(@RequestBody Worker w) {
        Worker result = new WorkerRepo().loginWorker(w);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getWorker/{id}")
    public Worker getWorker(@PathVariable int id) {
        return new WorkerRepo().getWorker(id);
    }

    @PostMapping("/changePassword")
    public Message changePassword(@RequestBody ChangePasswordObject obj) {
        return new WorkerRepo().changePassword(obj);
    }

    @PostMapping("/updateWorker")
    public Message updateWorker(@RequestBody Worker w) {
        return new WorkerRepo().updateWorker(w);
    }

    @PostMapping(value = "/registerWorker", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerWorker(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String nameOfPlace,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String mb,
            @RequestParam String pib,
            @RequestParam(required = false) Integer existingFacilityId,
            @RequestParam(required = false) MultipartFile image
    ) {
        WorkerRepo repo = new WorkerRepo();

        if (repo.usernameExists(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message(false,"Username already taken."));
        }
        if (repo.emailExists(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message(false,"Email already registered."));
        }
        String[] existingCredentials = repo.getExistingFacilityCredentials(nameOfPlace, address);
        if (existingCredentials != null) {
            if (!existingCredentials[0].equals(mb) || !existingCredentials[1].equals(pib)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Message(false,"Registration number and Tax ID must match the existing employee's data for this facility."));
            }
        }
        if (repo.maticniBrojExists(mb, nameOfPlace, address)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message(false,"Registration number already in use."));
        }
        if (repo.pibExists(pib, nameOfPlace, address)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message(false,"Tax ID already in use."));
        }

        if (repo.countWorkersAtFacility(nameOfPlace, address) >= 2) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new Message(false,"This facility already has the maximum number of registered employees (2)."));
        }

        Worker w = new Worker();
        w.setUsername(username);
        w.setPassword(password);
        w.setFirstname(firstname);
        w.setLastname(lastname);
        w.setEmail(email);
        w.setPhone(phone);
        w.setFacilityName(nameOfPlace);
        w.setAddress(address);
        w.setCity(city);
        w.setRegistrationNumber(mb);
        w.setTaxId(pib);

        if (image != null && !image.isEmpty()) {
            try {
                String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Files.write(Paths.get("images/" + filename), image.getBytes());
                w.setProfileImage(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int userId = repo.registerWorker(w);

        if (userId > 0) {
            return ResponseEntity.ok(userId);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message(false,"Registration failed."));
    }

    @PostMapping("/uploadProfileImage")
    public String uploadProfileImage(@RequestParam String username,
                                    @RequestParam MultipartFile image) {
        return new UserRepo().uploadProfileImage(username, image);
    }
    
    @GetMapping("/getMyFacilities/{workerId}")
    public ResponseEntity<?> getMyFacilities(@PathVariable int workerId) {
        FacilityRepo facilityRepo = new FacilityRepo();
        CourtRepo courtRepo = new CourtRepo();

        List<Facility> facilities = facilityRepo.getFacilitiesByWorkerId(workerId);
        if (facilities.isEmpty()) {
            return ResponseEntity.status(404).body("No facilities found for this worker");
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (Facility facility : facilities) {
            List<Court> courts = courtRepo.getCourtsForFacility(facility.getId());

            List<Court> openCourts = new ArrayList<>();
            List<Court> closedCourts = new ArrayList<>();
            List<Court> halls = new ArrayList<>();

            for (Court c : courts) {
                switch (c.getType()) {
                    case "OPEN" -> openCourts.add(c);
                    case "CLOSED" -> closedCourts.add(c);
                    case "HALL" -> halls.add(c);
                }
            }

            boolean hasValidOpenCourt = openCourts.stream().anyMatch(c -> c.getCapacity() >= 4);
            boolean closedNamesUnique = closedCourts.stream().map(Court::getName).distinct().count() == closedCourts.size();
            boolean hallNamesUnique = halls.stream().map(Court::getName).distinct().count() == halls.size();

            Map<String, Object> facilityData = new HashMap<>();
            facilityData.put("facility", facility);
            facilityData.put("openCourts", openCourts);
            facilityData.put("closedCourts", closedCourts);
            facilityData.put("halls", halls);
            facilityData.put("hasValidOpenCourt", hasValidOpenCourt);
            facilityData.put("closedNamesUnique", closedNamesUnique);
            facilityData.put("hallNamesUnique", hallNamesUnique);

            result.add(facilityData);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/getFacilitiesAvailableForSecondWorker")
    public List<FacilityWorkerOption> getFacilitiesAvailableForSecondWorker() {
        return new WorkerRepo().getFacilitiesAvailableForSecondWorker();
    }
}
