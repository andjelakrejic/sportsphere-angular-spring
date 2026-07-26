package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.backend.db.DB;
import com.example.backend.models.Court;
import com.example.backend.models.Facility;
import com.example.backend.models.Message;
import com.example.backend.models.Sport;
import com.example.backend.models.helpers.CourtOccupancyDTO;
import com.example.backend.models.helpers.FacilityUploadDTO;

public class FacilityRepo implements FacilityRepoInterface {

    @Override
    public List<Facility> getActiveFacilities() {
         try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("select * from facility where status='ACTIVE'");
        ) {
            List<Facility> facilities = new ArrayList<>();

            ResultSet rs = stm.executeQuery();

            while(rs.next()){
               // i konstruktor bez like_count
                Facility f = new Facility(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getString("address"),
                    rs.getString("description"),
                    rs.getString("working_hours_from"),
                    rs.getString("working_hours_to"),
                    rs.getDouble("price_per_hour"),
                    rs.getString("status")
                );
                facilities.add(f);
            }
            return facilities;
        
       } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Facility> getTop3Facilities() {
        List<Facility> facilities = new ArrayList<>();
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT f.*, COUNT(r.id) as like_count " +
                "FROM facility f " +
                "LEFT JOIN facility_reaction r ON f.id = r.facility_id AND r.type = 'LIKE' " +
                "WHERE f.status = 'ACTIVE' " +
                "GROUP BY f.id " +
                "ORDER BY like_count DESC " +
                "LIMIT 3"  // ovo mora biti ovde!
            );
        ) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Facility f = new Facility(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getString("address"),
                    rs.getString("description"),
                    rs.getString("working_hours_from"),
                    rs.getString("working_hours_to"),
                    rs.getDouble("price_per_hour"),  // double
                    rs.getString("status"),          // String
                    rs.getInt("like_count")
                );
                facilities.add(f);
            }
            return facilities;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Facility> getPendingFacilities() {
        List<Facility> facilities = new ArrayList<>();
        String sql = "SELECT * FROM facility WHERE status = 'PENDING'";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)) {

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Facility f = new Facility();
                f.setId(rs.getInt("id"));
                f.setName(rs.getString("name"));
                f.setCity(rs.getString("city"));
                f.setAddress(rs.getString("address"));
                f.setDescription(rs.getString("description"));
                f.setWorkingHoursFrom(rs.getString("working_hours_from"));
                f.setWorkingHoursTo(rs.getString("working_hours_to"));
                f.setPricePerHour(rs.getDouble("price_per_hour"));
                f.setMaxNoShows(rs.getInt("max_no_shows"));
                f.setStatus(rs.getString("status"));
                facilities.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facilities;
    }
    
    @Override
    public List<String> getActiveCities() {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT DISTINCT city FROM facility WHERE status='ACTIVE' ORDER BY city"
            );
        ) {
            List<String> cities = new ArrayList<>();
            ResultSet rs = stm.executeQuery();
           
            while (rs.next()) {
                cities.add(rs.getString("city"));
            }
            return cities;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getAllSports() {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT DISTINCT name FROM sport ORDER BY name"
            );
        ) {
            List<String> sports = new ArrayList<>();
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                sports.add(rs.getString("name"));
            }
            return sports;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Sport> getAllSportsObject() {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT * FROM sport"
            );
        ) {
            List<Sport> sports = new ArrayList<>();
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Sport s = new Sport(
                    rs.getInt("id"),
                    rs.getString("name")
                );
                sports.add(s);
            }
            return sports;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Sport> getSportsForFacility(int id) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT s.id as id, s.name as name " +
                "FROM sport s JOIN facility_sport fs ON fs.sport_id = s.id " +
                "WHERE fs.facility_id = ? " 
            );
        ) {
            stm.setInt(1, id);

            List<Sport> sports = new ArrayList<>();
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Sport s = new Sport(
                    rs.getInt("id"),
                    rs.getString("name")
                );
                sports.add(s);
            }
            return sports;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Facility getFacility(int id) {
         try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT * FROM facility WHERE id=?"
            );
        ) {
            stm.setInt(1, id);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Facility(
                    rs.getInt("id"), 
                    rs.getString("name"), 
                    rs.getString("city"),
                    rs.getString("address"), 
                    rs.getString("description"),
                    rs.getString("working_hours_from"), 
                    rs.getString("working_hours_to"),
                    rs.getDouble("price_per_hour"), 
                    rs.getString("status")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getFacilityImages(int id) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT image_url FROM facility_image WHERE facility_id = ?"
            );
        ) {
            stm.setInt(1, id);
            List<String> images = new ArrayList<>();
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                images.add(rs.getString("image_url"));
            }
            return images;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Facility> searchFacilities(String name, String city, String sport, String type) {
        try (Connection conn = DB.source().getConnection()) {

            StringBuilder query = new StringBuilder(
                "SELECT f.*, " +
                "GROUP_CONCAT(DISTINCT s.name SEPARATOR ', ') as sports, " +
                "GROUP_CONCAT(DISTINCT c.type SEPARATOR ', ') as court_types " +
                "FROM facility f " +
                "LEFT JOIN facility_sport fs ON f.id = fs.facility_id " +
                "LEFT JOIN sport s ON fs.sport_id = s.id " +
                "LEFT JOIN court c ON f.id = c.facility_id " +
                "WHERE f.status = 'ACTIVE'"
            );

            if (name != null && !name.isEmpty())
                query.append(" AND f.name LIKE ?");
            if (city != null && !city.isEmpty())
                query.append(" AND f.city = ?");
            if (sport != null && !sport.isEmpty())
                query.append(" AND f.id IN (SELECT fs2.facility_id FROM facility_sport fs2 " +
                            "JOIN sport s2 ON fs2.sport_id = s2.id WHERE s2.name = ?)");
            if (type != null && !type.isEmpty())
                query.append(" AND f.id IN (SELECT c2.facility_id FROM court c2 WHERE c2.type = ?)");

            query.append(" GROUP BY f.id");

            PreparedStatement stm = conn.prepareStatement(query.toString());

            List<Facility> facilities = new ArrayList<>();
            int index = 1;
            if (name != null && !name.isEmpty())
                stm.setString(index++, "%" + name + "%");
            if (city != null && !city.isEmpty())
                stm.setString(index++, city);
            if (sport != null && !sport.isEmpty())
                stm.setString(index++, sport);
            if (type != null && !type.isEmpty())
                stm.setString(index++, type);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Facility f = new Facility(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getString("address"),
                    rs.getString("description"),
                    rs.getString("working_hours_from"),
                    rs.getString("working_hours_to"),
                    rs.getDouble("price_per_hour"),
                    rs.getString("status")
                );
                f.setSports(rs.getString("sports"));
                f.setType(rs.getString("court_types")); // dodaj setter/polje
                facilities.add(f);
            }
            return facilities;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Facility> searchFreeTodayFacilities(String name, String city, String sport, String type) {
        try (Connection conn = DB.source().getConnection()) {

            StringBuilder query = new StringBuilder(
                "SELECT f.*, " +
                "GROUP_CONCAT(DISTINCT s.name SEPARATOR ', ') as sports, " +
                "GROUP_CONCAT(DISTINCT c.type SEPARATOR ', ') as court_types " +
                "FROM facility f " +
                "LEFT JOIN facility_sport fs ON f.id = fs.facility_id " +
                "LEFT JOIN sport s ON fs.sport_id = s.id " +
                "LEFT JOIN court c ON f.id = c.facility_id " +
                "WHERE f.status = 'ACTIVE' " +
                "AND EXISTS (" +
                "  SELECT 1 FROM court c2 WHERE c2.facility_id = f.id " +
                "  AND NOT EXISTS (" +
                "    SELECT 1 FROM reservation r WHERE r.court_id = c2.id " +
                "    AND r.date = CURDATE() AND r.status != 'CANCELLED'" +
                "  )" +
                ")"
            );

            if (name != null && !name.isEmpty())
                query.append(" AND f.name LIKE ?");
            if (city != null && !city.isEmpty())
                query.append(" AND f.city = ?");
            if (sport != null && !sport.isEmpty())
                query.append(" AND f.id IN (SELECT fs2.facility_id FROM facility_sport fs2 " +
                            "JOIN sport s2 ON fs2.sport_id = s2.id WHERE s2.name = ?)");
            if (type != null && !type.isEmpty())
                query.append(" AND f.id IN (SELECT c2.facility_id FROM court c2 WHERE c2.type = ?)");

            query.append(" GROUP BY f.id");

            PreparedStatement stm = conn.prepareStatement(query.toString());

            List<Facility> facilities = new ArrayList<>();

            int index = 1;
            if (name != null && !name.isEmpty())
                stm.setString(index++, "%" + name + "%");
            if (city != null && !city.isEmpty())
                stm.setString(index++, city);
            if (sport != null && !sport.isEmpty())
                stm.setString(index++, sport);
            if (type != null && !type.isEmpty())
                stm.setString(index++, type);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Facility f = new Facility(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getString("address"),
                    rs.getString("description"),
                    rs.getString("working_hours_from"),
                    rs.getString("working_hours_to"),
                    rs.getDouble("price_per_hour"),
                    rs.getString("status")
                );
                f.setSports(rs.getString("sports"));
                f.setType(rs.getString("court_types"));
                facilities.add(f);
            }
            return facilities;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

   @Override
    public Message addFacility(FacilityUploadDTO dto, int workerId) {
        Connection conn = null;
        try {
            conn = DB.source().getConnection();
            conn.setAutoCommit(false); // pocetak transakcije

            String sql = "INSERT INTO facility (name, city, address, description, " +
                        "working_hours_from, working_hours_to, price_per_hour, max_no_shows, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'PENDING')";

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getName());
            ps.setString(2, dto.getCity());
            ps.setString(3, dto.getAddress());
            ps.setString(4, dto.getDescription());
            ps.setString(5, dto.getWorkingHoursFrom());
            ps.setString(6, dto.getWorkingHoursTo());
            ps.setDouble(7, dto.getPricePerHour());
            ps.setInt(8, dto.getMaxNoShows());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                conn.rollback();
                return new Message(false, "Failed to create facility.");
            }

            int facilityId;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    conn.rollback();
                    return new Message(false, "Failed to retrieve new facility id.");
                }
                facilityId = keys.getInt(1);
            }

            // 1. Poveži worker-a sa novim objektom preko worker_facility tabele
            PreparedStatement linkStm = conn.prepareStatement(
                "insert into worker_facility (worker_id, facility_id) values (?, ?)"
            );
            linkStm.setInt(1, workerId);
            linkStm.setInt(2, facilityId);
            linkStm.executeUpdate();

            // 2. Ako je poslata i lista terena, validiraj i ubaci
            if (dto.getCourts() != null && !dto.getCourts().isEmpty()) {
                Message courtsValidation = validateCourtNamesUnique(dto.getCourts());
                if (!courtsValidation.isSuccess()) {
                    conn.rollback();
                    return courtsValidation;
                }

                for (FacilityUploadDTO.CourtDTO c : dto.getCourts()) {
                    if (c.getEquipmentDescription() != null && c.getEquipmentDescription().length() > 300) {
                        conn.rollback();
                        return new Message(false,
                            "Equipment description for court '" + c.getName() + "' exceeds 300 characters.");
                    }

                    Court court = new Court();
                    court.setFacilityId(facilityId);
                    court.setName(c.getName());
                    court.setType(c.getType());
                    court.setCapacity(c.getCapacity());
                    court.setEquipmentDescription(c.getEquipmentDescription());
                    court.setSportId(c.getSportId());

                    // Koristi istu konekciju kako bi sve bilo u istoj transakciji
                    // addCourt sada i sam upisuje vezu u facility_sport
                    Message courtResult = addCourt(conn, court);
                    if (!courtResult.isSuccess()) {
                        conn.rollback();
                        return courtResult;
                    }
                }
            }

            conn.commit();
            return new Message(true, "Facility submitted for approval.");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return new Message(false, "Database error: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    @Override
    public Message addCourt(Court court) {
        try (Connection conn = DB.source().getConnection()) {
            return addCourt(conn, court);
        } catch (SQLException e) {
            return new Message(false, "Database error: " + e.getMessage());
        }
    }

    // Ova metoda deli konekciju i transakciju sa addFacility metodom
    public Message addCourt(Connection conn, Court court) throws SQLException {
        if (court.getEquipmentDescription() != null && court.getEquipmentDescription().length() > 300) {
            return new Message(false, "Equipment description exceeds 300 characters.");
        }

        String checkSql = "SELECT COUNT(*) FROM court WHERE facility_id = ? AND LOWER(TRIM(name)) = LOWER(TRIM(?))";
        String insertSql = "INSERT INTO court (facility_id, name, type, capacity, equipment_description, sport_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
        String linkSql = "INSERT INTO facility_sport (facility_id, sport_id) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE facility_id = facility_id";

        try (PreparedStatement check = conn.prepareStatement(checkSql)) {
            check.setInt(1, court.getFacilityId());
            check.setString(2, court.getName());
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return new Message(false,
                        "A court/hall with name '" + court.getName() + "' already exists in this facility.");
                }
            }
        }

        try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
            insert.setInt(1, court.getFacilityId());
            insert.setString(2, court.getName());
            insert.setString(3, court.getType());
            insert.setInt(4, court.getCapacity());
            insert.setString(5, court.getEquipmentDescription());
            insert.setInt(6, court.getSportId());

            int rows = insert.executeUpdate();
            if (rows == 0) {
                return new Message(false, "Failed to add court.");
            }
        }

        // Poveži facility sa sportom ovog terena (upsert, bez duplikata zahvaljujući composite PK)
        try (PreparedStatement link = conn.prepareStatement(linkSql)) {
            link.setInt(1, court.getFacilityId());
            link.setInt(2, court.getSportId());
            link.executeUpdate();
        }

        return new Message(true, "Court added successfully.");
    }

    // provera unikatnosti naziva unutar liste koja se salje odjednom (JSON upload)
    private Message validateCourtNamesUnique(List<FacilityUploadDTO.CourtDTO> courts) {
        Set<String> seen = new HashSet<>();
        for (FacilityUploadDTO.CourtDTO c : courts) {
            String key = c.getName() == null ? "" : c.getName().trim().toLowerCase();
            if (key.isEmpty()) {
                return new Message(false, "Court name cannot be empty.");
            }
            if (!seen.add(key)) {
                return new Message(false, "Duplicate court name in upload: '" + c.getName() + "'.");
            }
        }
        return new Message(true, "OK");
    }

    @Override
    public Message updateFacility(Facility facility) {
        String sql = "UPDATE facility SET name = ?, city = ?, address = ?, description = ?, " +
                     "working_hours_from = ?, working_hours_to = ?, price_per_hour = ?, max_no_shows = ? " +
                     "WHERE id = ?";
        try (Connection conn = DB.source().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, facility.getName());
            ps.setString(2, facility.getCity());
            ps.setString(3, facility.getAddress());
            ps.setString(4, facility.getDescription());
            ps.setString(5, facility.getWorkingHoursFrom());
            ps.setString(6, facility.getWorkingHoursTo());
            ps.setDouble(7, facility.getPricePerHour());
            ps.setInt(8, facility.getMaxNoShows());
            ps.setInt(9, facility.getId());

            int rows = ps.executeUpdate();
            return rows > 0
                ? new Message(true, "Facility updated successfully.")
                : new Message(false, "Facility not found.");

        } catch (SQLException e) {
            return new Message(false, "Database error: " + e.getMessage());
        }
    }

    @Override
    public List<Facility> getFacilitiesByWorkerId(int workerId) {
        List<Facility> facilities = new ArrayList<>();
        String sql = "SELECT f.* FROM facility f " +
                    "JOIN worker_facility wf ON f.id = wf.facility_id " +
                    "WHERE wf.worker_id = ?";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Facility f = new Facility();
                    f.setId(rs.getInt("id"));
                    f.setName(rs.getString("name"));
                    f.setCity(rs.getString("city"));
                    f.setAddress(rs.getString("address"));
                    f.setDescription(rs.getString("description"));
                    f.setWorkingHoursFrom(rs.getString("working_hours_from"));
                    f.setWorkingHoursTo(rs.getString("working_hours_to"));
                    f.setPricePerHour(rs.getDouble("price_per_hour"));
                    f.setMaxNoShows(rs.getInt("max_no_shows"));
                    f.setStatus(rs.getString("status"));
                    facilities.add(f);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facilities;
    }

    @Override
    public List<Court> getCourtsByFacilityId(int facilityId) {
        List<Court> courts = new ArrayList<>();
        String sql = "SELECT * FROM court WHERE facility_id = ? ";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
            {
                ps.setInt(1, facilityId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Court c = new Court();
                    c.setId(rs.getInt("id"));
                    c.setFacilityId(rs.getInt("facility_id"));
                    c.setName(rs.getString("name"));
                    c.setType(rs.getString("type"));
                    c.setCapacity(rs.getInt("capacity"));
                    c.setEquipmentDescription(rs.getString("equipment_description"));
                    c.setSportId(rs.getInt("sport_id"));
                    courts.add(c);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        return courts;
    }

    @Override
    public Message updateCourt(Court court) {
        String checkSql = "SELECT COUNT(*) FROM court WHERE facility_id = ? AND LOWER(TRIM(name)) = LOWER(TRIM(?)) AND id != ? ";
        String updateSql = "UPDATE court SET name = ?, type = ?, capacity = ?, equipment_description = ?, sport_id = ? WHERE id = ?";

        if (court.getEquipmentDescription() != null && court.getEquipmentDescription().length() > 300) {
            return new Message(false, "Equipment description exceeds 300 characters.");
        }

        try (Connection conn = DB.source().getConnection()) {

            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setInt(1, court.getFacilityId());
                check.setString(2, court.getName());
                check.setInt(3, court.getId());
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return new Message(false, "A court/hall with this name already exists in this facility.");
                    }
                }
            }

            try (PreparedStatement update = conn.prepareStatement(updateSql)) {
                update.setString(1, court.getName());
                update.setString(2, court.getType());
                update.setInt(3, court.getCapacity());
                update.setString(4, court.getEquipmentDescription());
                update.setInt(5, court.getSportId());
                update.setInt(6, court.getId());

                int rows = update.executeUpdate();
                return rows > 0
                    ? new Message(true, "Court updated successfully.")
                    : new Message(false, "Court not found.");
            }

        } catch (SQLException e) {
            return new Message(false, "Database error: " + e.getMessage());
        }
    }

    @Override
    public List<CourtOccupancyDTO> getFacilityOccupancy(int facilityId, LocalDate monthStart, LocalDate monthEnd) {
        List<CourtOccupancyDTO> result = new ArrayList<>();

        // working hours objekta
        String facilitySql = "SELECT working_hours_from, working_hours_to FROM facility WHERE id = ?";
        // sati po terenu iz rezervacija
        String reservationSql =
            "SELECT court_id, SUM(TIME_TO_SEC(TIMEDIFF(time_to, time_from)))/3600.0 AS hours " +
            "FROM reservation " +
            "WHERE date BETWEEN ? AND ? AND status != 'CANCELLED' " +
            "GROUP BY court_id";
        // sati po terenu iz treninga
        String trainingSql =
            "SELECT court_id, SUM(TIME_TO_SEC(TIMEDIFF(time_to, time_from)))/3600.0 AS hours " +
            "FROM individual_training " +
            "WHERE training_date BETWEEN ? AND ? AND court_id IS NOT NULL " +
            "GROUP BY court_id";
        // svi tereni objekta
        String courtsSql = "SELECT id, name, type FROM court WHERE facility_id = ?";

        try (Connection conn = DB.source().getConnection()) {

            double hoursPerDay;
            try (PreparedStatement stm = conn.prepareStatement(facilitySql)) {
                stm.setInt(1, facilityId);
                ResultSet rs = stm.executeQuery();
                if (!rs.next()) return result;
                LocalTime from = rs.getTime("working_hours_from").toLocalTime();
                LocalTime to = rs.getTime("working_hours_to").toLocalTime();
                hoursPerDay = java.time.Duration.between(from, to).toMinutes() / 60.0;
            }

            long daysInMonth = java.time.temporal.ChronoUnit.DAYS.between(monthStart, monthEnd) + 1;
            double availableHours = hoursPerDay * daysInMonth;

            Map<Integer, Double> reservationHours = new HashMap<>();
            try (PreparedStatement stm = conn.prepareStatement(reservationSql)) {
                stm.setDate(1, java.sql.Date.valueOf(monthStart));
                stm.setDate(2, java.sql.Date.valueOf(monthEnd));
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    reservationHours.put(rs.getInt("court_id"), rs.getDouble("hours"));
                }
            }

            Map<Integer, Double> trainingHours = new HashMap<>();
            try (PreparedStatement stm = conn.prepareStatement(trainingSql)) {
                stm.setDate(1, java.sql.Date.valueOf(monthStart));
                stm.setDate(2, java.sql.Date.valueOf(monthEnd));
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    trainingHours.put(rs.getInt("court_id"), rs.getDouble("hours"));
                }
            }

            try (PreparedStatement stm = conn.prepareStatement(courtsSql)) {
                stm.setInt(1, facilityId);
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    int courtId = rs.getInt("id");
                    double booked = reservationHours.getOrDefault(courtId, 0.0)
                                + trainingHours.getOrDefault(courtId, 0.0);
                    result.add(new CourtOccupancyDTO(
                        rs.getString("name"),
                        rs.getString("type"),
                        booked,
                        availableHours
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
