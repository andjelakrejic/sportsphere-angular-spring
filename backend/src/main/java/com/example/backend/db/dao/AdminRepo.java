package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.example.backend.db.DB;
import com.example.backend.models.Admin;
import com.example.backend.models.Message;
import com.example.backend.models.Sport;

public class AdminRepo implements AdminRepoInterface {
    
    @Override
    public Admin loginAdmin(Admin a) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT u.id, u.username, u.password, u.firstname, u.lastname, u.email, u.phone, u.profile_image, " +
                "FROM user u " +
                "JOIN admin a ON u.id = a.user_id " +
                "WHERE u.username = ? AND u.role = 'ADMIN'");
        ) {
            
            stm.setString(1, a.getUsername());
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                String hashInDb = rs.getString("password");
                boolean match = BCrypt.checkpw(a.getPassword(), hashInDb);
                if (!match) return null;

                return new Admin(
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
    public Message addSport(Sport s) {
        Message m = new Message("");
         try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("insert into sport (id, name) values (?,?)");
        ) {
            stm.setInt(1, s.getId());
            stm.setString(2, s.getName());

            int x = stm.executeUpdate();
            if (x>0) m.setMessage("Sport '" + s.getName() + "' successfully inserted!");
            else m.setMessage("Error inserting sport...");
            

           } catch (SQLException e) {
            e.printStackTrace();
       }
       return m;
    }

    @Override
    public Message acceptRequest(int userId) {
         Message m = new Message("");
         try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("update user set status='APPROVED' where status='PENDING' and id=?");
        ) {
            stm.setInt(1, userId);

            int x = stm.executeUpdate();
            if (x>0) m.setMessage("Request from user with id: " + userId + " accepted");
            else m.setMessage("Error accepting request...");
            

           } catch (SQLException e) {
            e.printStackTrace();
       }
       return m;
    }

    @Override
    public Message denyRequest(int userId) {
         Message m = new Message("");
         try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("update user set status='DENIED' where status='PENDING' id=?");
        ) {
            stm.setInt(1, userId);

            int x = stm.executeUpdate();
            if (x>0) m.setMessage("Request from user with id: " + userId + " denied");
            else m.setMessage("Error denying request...");
            

           } catch (SQLException e) {
            e.printStackTrace();
       }
       return m;
    }

    @Override
    public Message acceptFacilityRequest() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'acceptFacilityRequest'");
    }

    @Override
    public Message changeAthleteAccount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeAthleteAccount'");
    }

    @Override
    public Message changeWorkerAccount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeWorkerAccount'");
    }

    @Override
    public Message deleteAthleteAccount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAthleteAccount'");
    }

    @Override
    public Message deleteWorkerAccount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteWorkerAccount'");
    }
}
