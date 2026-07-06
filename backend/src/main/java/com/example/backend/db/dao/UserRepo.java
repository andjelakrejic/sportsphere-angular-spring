package com.example.backend.db.dao;

import java.sql.Statement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.DB;
import com.example.backend.models.Athlete;
import com.example.backend.models.Message;
import com.example.backend.models.Worker;
import com.example.backend.models.helpers.ChangePasswordObject;
import com.example.backend.models.helpers.FavoriteSportsObject;

public class UserRepo implements UserRepoInterface {
    
    @Override
    public Athlete loginAthlete(Athlete a) {    
        try (Connection conn = DB.source().getConnection();
         PreparedStatement stm = conn.prepareStatement(
             "SELECT u.id, u.username, u.password, u.firstname, u.lastname, u.email, u.phone, u.profile_image " +
            "FROM user u " +
            "JOIN athlete at ON u.id = at.user_id " +
            "WHERE u.username = ? AND u.role = 'ATHLETE'"
         )) {

        stm.setString(1, a.getUsername());
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            String hashInDb = rs.getString("password");
            boolean match = BCrypt.checkpw(a.getPassword(), hashInDb);
            if (!match) return null;

            return new Athlete(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("profile_image")
            );
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Worker loginWorker(Worker w) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT u.id, u.username, u.password, u.firstname, u.lastname, u.email, u.phone, u.profile_image, " +
                "wk.facility_name, wk.address, wk.registration_number, wk.tax_id " +
                "FROM user u " +
                "JOIN worker wk ON u.id = wk.user_id " +
                "WHERE u.username = ? AND u.role = 'WORKER'"
            )) {

            stm.setString(1, w.getUsername());
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                String hashInDb = rs.getString("password");
                boolean match = BCrypt.checkpw(w.getPassword(), hashInDb);
                if (!match) return null;
                
                return new Worker(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("profile_image"),
                    rs.getString("facility_name"),
                    rs.getString("address"),
                    rs.getString("registration_number"),
                    rs.getString("tax_id")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadProfileImage(String username, MultipartFile image) {
        try {
            String uploadDir = "images/";
            Files.createDirectories(Paths.get(uploadDir));

            String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + filename);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            try (Connection conn = DB.source().getConnection();
                PreparedStatement stm = conn.prepareStatement(
                        "UPDATE user SET profile_image=? WHERE username=?")) {
                stm.setString(1, filename);
                stm.setString(2, username);
                stm.executeUpdate();
            }

            return filename;

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }  


    @Override
    public Worker getWorker(String username) {
         try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("select * from worker where username=?");
        ) {
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();

            if(rs.next()){
                return new Worker(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("profile_image"),
                    rs.getString("facility_name"),
                    rs.getString("address"),
                    rs.getString("registration_number"),
                    rs.getString("tax_id")
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Athlete getAthlete(String username) { // ne dohvata favoritesports iz tabele athlete_sport to dodaj jer ne radi athlete_profile u frontu
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("select * from athlete where username=?");
        ) {
            stm.setString(1, username);

            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return new Athlete(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("image")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Athlete getAthleteById(int id) { // ne dohvata favoritesports iz tabele athlete_sport to dodaj jer ne radi athlete_profile u frontu
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("select name from user where id=?");
        ) {
            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return new Athlete(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("image")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 1. Update osnovnih podataka (bez username-a)
    @Override
    public Message updateAthlete(Athlete a) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE user SET firstname=?, lastname=?, email=?, phone=?, profile_image=? WHERE username=? AND role='ATHLETE'")) {
            
            stm.setString(1, a.getFirstname());
            stm.setString(2, a.getLastname());
            stm.setString(3, a.getEmail());
            stm.setString(4, a.getPhone());
            stm.setString(5, a.getProfileImage());
            stm.setString(6, a.getUsername());

            int rows = stm.executeUpdate();
            return rows > 0 ? new Message("Athlete successfully updated!") : new Message("Error updating athlete");

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Message updateFavoriteSports(FavoriteSportsObject obj) {
        try (Connection conn = DB.source().getConnection()) {

            // Obriši stare
            try (PreparedStatement del = conn.prepareStatement(
                    "DELETE FROM athlete_sport WHERE athlete_id=?")) {
                del.setInt(1, obj.getAthleteId());
                del.executeUpdate();
            }

            // Ubaci nove
            try (PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO athlete_sport (athlete_id, sport_id) VALUES (?, ?)")) {
                for (int sportId : obj.getSportIds()) {
                    ins.setInt(1, obj.getAthleteId());
                    ins.setInt(2, sportId);
                    ins.addBatch();
                }
                ins.executeBatch();
            }

            return new Message("Sports list updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 3. Promena lozinke — odvojeno jer zahteva proveru stare
    public Message changePassword(ChangePasswordObject obj) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement check = conn.prepareStatement(
                "SELECT 1 FROM user WHERE username=? AND password=?")) {
            
            check.setString(1, obj.getUsername());
            check.setString(2, obj.getOldPass());
            ResultSet rs = check.executeQuery();

            if (!rs.next()) return new Message("You entered the wrong current password");

            try (PreparedStatement upd = conn.prepareStatement(
                    "UPDATE user SET password=? WHERE username=?")) {
                upd.setString(1, obj.getNewPass());
                upd.setString(2, obj.getUsername());
                upd.executeUpdate();
            }

            return new Message("Password changed");

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Register

    @Override
    public int registerAthlete(Athlete a) {
        try (Connection conn = DB.source().getConnection()) {
            conn.setAutoCommit(false); // transakcija - oba inserta ili nijedan

            // 1. insert u user tabelu
            String userSql = "INSERT INTO user (username, password, firstname, lastname, email, phone, profile_image, status, role) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stm1 = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);

            String hashedPassword = BCrypt.hashpw(a.getPassword(), BCrypt.gensalt());

            stm1.setString(1, a.getUsername());
            stm1.setString(2, hashedPassword);
            stm1.setString(3, a.getFirstname());
            stm1.setString(4, a.getLastname());
            stm1.setString(5, a.getEmail());
            stm1.setString(6, a.getPhone());
            stm1.setString(7, a.getProfileImage() != null ? a.getProfileImage() : "default-avatar.png");
            stm1.setString(8, "PENDING");
            stm1.setString(9, "ATHLETE");

            int rows = stm1.executeUpdate();
            if (rows == 0) {
                conn.rollback();
                return 0;
            }

            // 2. uzmi generisani user_id
            ResultSet generatedKeys = stm1.getGeneratedKeys();
            int userId;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return 0;
            }

            // 3. insert u athlete tabelu
            PreparedStatement stm2 = conn.prepareStatement(
                "INSERT INTO athlete (user_id) VALUES (?)"
            );
            stm2.setInt(1, userId);
            stm2.executeUpdate();

            conn.commit();
            return userId;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int registerWorker(Worker w) {
        try (Connection conn = DB.source().getConnection()) {
            conn.setAutoCommit(false);

            String userSql = "INSERT INTO user (username, password, firstname, lastname, email, phone, profile_image, status, role) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stm1 = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);

            String hashedPassword = BCrypt.hashpw(w.getPassword(), BCrypt.gensalt());

            stm1.setString(1, w.getUsername());
            stm1.setString(2, hashedPassword);
            stm1.setString(3, w.getFirstname());
            stm1.setString(4, w.getLastname());
            stm1.setString(5, w.getEmail());
            stm1.setString(6, w.getPhone());
            stm1.setString(7, w.getProfileImage() != null ? w.getProfileImage() : "default-avatar.png");
            stm1.setString(8, "PENDING");
            stm1.setString(9, "WORKER");

            int rows = stm1.executeUpdate();
            if (rows == 0) {
                conn.rollback();
                return 0;
            }

            ResultSet generatedKeys = stm1.getGeneratedKeys();
            int userId;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return 0;
            }

            PreparedStatement stm2 = conn.prepareStatement(
                "INSERT INTO worker (user_id, facility_name, address, registration_number, tax_id) VALUES (?, ?, ?, ?, ?)"
            );
            stm2.setInt(1, userId);
            stm2.setString(2, w.getFacilityName());
            stm2.setString(3, w.getAddress());
            stm2.setString(4, w.getRegistrationNumber());
            stm2.setString(5, w.getTaxId());
            stm2.executeUpdate();

            conn.commit();
            return userId;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean usernameExists(String username) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT id FROM user WHERE username = ? AND status != 'REJECTED'")
        ) {
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {  
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean emailExists(String email) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT id FROM user WHERE email = ? AND status != 'REJECTED'")
        ) {
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean maticniBrojExists(String mb) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT w.user_id FROM worker w JOIN user u ON w.user_id = u.id " +
                "WHERE w.registration_number = ? AND u.status != 'REJECTED'")
        ) {
            stm.setString(1, mb);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean pibExists(String pib) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT w.user_id FROM worker w JOIN user u ON w.user_id = u.id " +
                "WHERE w.tax_id = ? AND u.status != 'REJECTED'")
        ) {
            stm.setString(1, pib);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void addFavoriteSport(int athleteId, int sportId) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "INSERT INTO athlete_sport (athlete_id, sport_id) VALUES (?, ?)")
        ) {
            stm.setInt(1, athleteId);
            stm.setInt(2, sportId);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int countWorkersAtFacility(String facilityName, String address) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT COUNT(*) FROM worker w JOIN user u ON w.user_id = u.id " +
                "WHERE LOWER(TRIM(w.facility_name)) = LOWER(TRIM(?)) " +
                "AND LOWER(TRIM(w.address)) = LOWER(TRIM(?)) " +
                "AND u.status != 'REJECTED'")
        ) {
            stm.setString(1, facilityName);
            stm.setString(2, address);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 999; // fail-safe: ako provera pukne, blokiraj registraciju umesto da propusti treći
    }

}
