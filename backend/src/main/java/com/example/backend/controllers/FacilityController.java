package com.example.backend.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.dao.FacilityRepo;
import com.example.backend.models.Court;
import com.example.backend.models.Facility;
import com.example.backend.models.Message;
import com.example.backend.models.Sport;
import com.example.backend.models.helpers.CourtOccupancyDTO;
import com.example.backend.models.helpers.FacilityUploadDTO;
import com.example.backend.services.ReportGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;



@RestController
@RequestMapping("/facilities")
@CrossOrigin(origins = "http://localhost:4200")
public class FacilityController {

    @GetMapping("/getActiveFacilities")
    public List<Facility> getActiveFacilities() {
        return new FacilityRepo().getActiveFacilities();
    }

    @GetMapping("/getTop3Facilities")
    public List<Facility> getTop3Facilities() {
        return new FacilityRepo().getTop3Facilities();
    }

    @GetMapping("/getActiveCities")
    public List<String> getActiveCities() {
       return new FacilityRepo().getActiveCities();
    }

    @GetMapping("/getSportsForFacility/{id}")
    public List<Sport> getSportsForFacility(@PathVariable int id) {
       return new FacilityRepo().getSportsForFacility(id);
    }

    @GetMapping("/getAllSports")
    public List<String> getAllSports() {
       return new FacilityRepo().getAllSports();
    }

    @GetMapping("/getAllSportsObject")
    public List<Sport> getAllSportsObject() {
       return new FacilityRepo().getAllSportsObject();
    }

    @GetMapping("/searchFacility")
    public List<Facility> searchFacilities(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String sport,
        @RequestParam(required = false) String type
    ) {
        return new FacilityRepo().searchFacilities(name, city, sport, type);
    }

    @GetMapping("/searchFreeTodayFacility")
    public List<Facility> searchFreeTodayFacilities(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String sport,
        @RequestParam(required = false) String type
    ) {
        return new FacilityRepo().searchFreeTodayFacilities(name, city, sport, type);
    }

    @GetMapping("/getFacility/{facilityId}")
    public Facility getFacility(@PathVariable int facilityId) {
        return new FacilityRepo().getFacility(facilityId);
    }

    @GetMapping("/getFacilityImages/{facilityId}")
    public List<String> getFacilityImages(@PathVariable int facilityId) {
        return new FacilityRepo().getFacilityImages(facilityId);
    }
    
    // worker-facilities
    @PostMapping("/addFacility")
    public Message addFacility(@RequestBody FacilityUploadDTO dto, @RequestParam int workerId) {
        return new FacilityRepo().addFacility(dto, workerId);
    }

    @PostMapping(value = "/uploadFacilityJson", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Message uploadFacilityJson(@RequestParam("file") MultipartFile file, @RequestParam int workerId) {
        if (file.isEmpty()) {
            return new Message(false, "Uploaded file is empty. Please select a valid JSON file.");
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            
            // Parse JSON file into DTO
            FacilityUploadDTO dto = objectMapper.readValue(file.getInputStream(), FacilityUploadDTO.class);
            
            if (dto == null) {
                return new Message(false, "JSON file does not contain valid facility data.");
            }

            // Call database transaction for facility, worker_facility, court, and facility_sport
            return new FacilityRepo().addFacility(dto, workerId);

        } catch (JsonParseException | JsonMappingException e) {
            // Catch JSON syntax errors (missing quotes, brackets, bad formatting, etc.)
            return new Message(false, "Invalid JSON format: " + e.getOriginalMessage());
        } catch (IOException e) {
            return new Message(false, "Failed to read JSON file: " + e.getMessage());
        }
    }

    @PutMapping("/updateFacility")
    public Message updateFacility(@RequestBody Facility facility) {
        return new FacilityRepo().updateFacility(facility);
    }

    @GetMapping("/getFacilitiesByWorker/{workerId}")
    public List<Facility> getFacilitiesByWorker(@PathVariable int workerId) {
        return new FacilityRepo().getFacilitiesByWorkerId(workerId);
    }

    @GetMapping("/getCourtsByFacility/{facilityId}")
    public List<Court> getCourtsByFacility(@PathVariable int facilityId) {
        return new FacilityRepo().getCourtsByFacilityId(facilityId);
    }

    @PostMapping("/addCourt")
    public Message addCourt(@RequestBody Court court) {
        return new FacilityRepo().addCourt(court);
    }

    @PutMapping("/updateCourt")
    public Message updateCourt(@RequestBody Court court) {
        return new FacilityRepo().updateCourt(court);
    }
    
    @GetMapping("/reports/occupancy")
    public ResponseEntity<byte[]> getOccupancyReport(
            @RequestParam int facilityId,
            @RequestParam String month
    ) {
        try {
            YearMonth ym = YearMonth.parse(month);
            LocalDate monthStart = ym.atDay(1);
            LocalDate monthEnd = ym.atEndOfMonth();

            Facility facility = new FacilityRepo().getFacility(facilityId);
            List<CourtOccupancyDTO> data = new FacilityRepo().getFacilityOccupancy(facilityId, monthStart, monthEnd);

            byte[] pdf = new ReportGenerator().generateOccupancyReport(facility.getName(), monthStart, monthEnd, data);

            return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=izvestaj-popunjenost.pdf")
                .body(pdf);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
