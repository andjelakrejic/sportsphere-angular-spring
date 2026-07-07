package com.example.backend.db.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.DB;
import com.example.backend.models.Message;
import com.example.backend.models.Worker;
import com.example.backend.models.helpers.ChangePasswordObject;

public class WorkerRepo implements WorkerRepoInterface{

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

            // pronađi ili kreiraj facility ---
            int facilityId;
            PreparedStatement findFacility = conn.prepareStatement(
                "SELECT id FROM facility WHERE LOWER(TRIM(name)) = LOWER(TRIM(?)) AND LOWER(TRIM(address)) = LOWER(TRIM(?))"
            );
            findFacility.setString(1, w.getFacilityName());
            findFacility.setString(2, w.getAddress());
            ResultSet facilityRs = findFacility.executeQuery();

            if (facilityRs.next()) {
                facilityId = facilityRs.getInt("id");
            } else {
                PreparedStatement createFacility = conn.prepareStatement(
                    "INSERT INTO facility (name, city, address, description, working_hours_from, working_hours_to, price_per_hour, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                createFacility.setString(1, w.getFacilityName());
                createFacility.setString(2, "");     // TODO: city - forma ga jos ne prikuplja
                createFacility.setString(3, w.getAddress());
                createFacility.setString(4, "");
                createFacility.setString(5, "08:00"); // TODO: default radno vreme
                createFacility.setString(6, "22:00");
                createFacility.setDouble(7, 0.0);      // TODO: default cena
                createFacility.setString(8, "PENDING");

                int facRows = createFacility.executeUpdate();
                if (facRows == 0) {
                    conn.rollback();
                    return 0;
                }
                ResultSet facKeys = createFacility.getGeneratedKeys();
                if (facKeys.next()) {
                    facilityId = facKeys.getInt(1);
                } else {
                    conn.rollback();
                    return 0;
                }
            }

            PreparedStatement stm2 = conn.prepareStatement(
                "INSERT INTO worker (user_id, facility_name, address, registration_number, tax_id, facility_id) VALUES (?, ?, ?, ?, ?, ?)"
            );
            stm2.setInt(1, userId);
            stm2.setString(2, w.getFacilityName());
            stm2.setString(3, w.getAddress());
            stm2.setString(4, w.getRegistrationNumber());
            stm2.setString(5, w.getTaxId());
            stm2.setInt(6, facilityId);
            stm2.executeUpdate();

            conn.commit();
            return userId;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
    public Worker getWorker(int userId) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT u.*, w.facility_name, w.address, w.registration_number, w.tax_id, w.facility_id " +
                "FROM user u JOIN worker w ON u.id = w.user_id WHERE u.id = ?"
            )) {
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Worker w = new Worker();
                w.setId(rs.getInt("id"));
                w.setUsername(rs.getString("username"));
                w.setFirstname(rs.getString("firstname"));
                w.setLastname(rs.getString("lastname"));
                w.setEmail(rs.getString("email"));
                w.setPhone(rs.getString("phone"));
                w.setProfileImage(rs.getString("profile_image"));
                w.setFacilityName(rs.getString("facility_name"));
                w.setAddress(rs.getString("address"));
                w.setRegistrationNumber(rs.getString("registration_number"));
                w.setTaxId(rs.getString("tax_id"));
                w.setFacilityId(rs.getInt("facility_id"));
                return w;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message updateWorker(Worker w) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE user SET firstname=?, lastname=?, email=?, phone=? WHERE id=?"
            )) {
            stm.setString(1, w.getFirstname());
            stm.setString(2, w.getLastname());
            stm.setString(3, w.getEmail());
            stm.setString(4, w.getPhone());
            stm.setInt(5, w.getId());
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0
                ? new Message(true, "Profile updated successfully.")
                : new Message(false, "Update failed.");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(false, "Server error.");
        }
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
