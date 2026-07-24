package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.example.backend.db.DB;
import com.example.backend.models.Admin;
import com.example.backend.models.Message;
import com.example.backend.models.Sport;
import com.example.backend.models.User;

public class AdminRepo implements AdminRepoInterface {
    
    @Override
    public Admin loginAdmin(Admin a) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT u.id, u.username, u.password, u.firstname, u.lastname, u.email, u.phone, u.profile_image " +
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
    public List<User> getPendingRequests() {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT * FROM user WHERE status = 'PENDING' AND role IN ('ATHLETE', 'WORKER')"
            );
        ) {
            List<User> users = new ArrayList<>();
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("profile_image"),
                    rs.getString("status"),
                    rs.getString("role")
                ));
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message addSport(Sport s) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("insert into sport (name) values (?)");
        ) {
            stm.setString(1, s.getName());

            int x = stm.executeUpdate();
            if (x > 0) m.setMessage("Sport '" + s.getName() + "' successfully inserted!");
            else m.setMessage("Error inserting sport...");

        } catch (SQLException e) {
            e.printStackTrace();
            m.setMessage("Database error: " + e.getMessage());
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
            PreparedStatement stm = conn.prepareStatement(
                "update user set status='REJECTED' where status='PENDING' and id=?"
            );
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
    public List<User> viewAllAccounts() {
         try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("select * from user where role !='ADMIN' and status = 'APPROVED'");
        ) {
            List<User> users = new ArrayList<>();

            ResultSet rs = stm.executeQuery();
            while (rs.next()){
                User u = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("profile_image"),
                    rs.getString("status"),
                     rs.getString("role")
                );
                users.add(u);
            }
            return users;
            

           } catch (SQLException e) {
            e.printStackTrace();
       }
       return null;
    }

    @Override
    public Message updateUser(User u) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE user SET firstname=?, lastname=?, email=?, phone=? WHERE id=?"
            );
        ) {
            stm.setString(1, u.getFirstname());
            stm.setString(2, u.getLastname());
            stm.setString(3, u.getEmail());
            stm.setString(4, u.getPhone());
            stm.setInt(5, u.getId());

            int x = stm.executeUpdate();
            if (x > 0) m.setMessage("User updated successfully!");
            else m.setMessage("Error updating user...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    @Override
    public Message deleteUser(int userId) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "DELETE FROM user WHERE id=?"
            );
        ) {
            stm.setInt(1, userId);

            int x = stm.executeUpdate();
            if (x > 0) m.setMessage("User deleted successfully!");
            else m.setMessage("Error deleting user...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    @Override
    public Message acceptFacilityRequest(int facilityId) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE facility SET status='ACTIVE' WHERE status='PENDING' AND id=?"
            );
        ) {
            stm.setInt(1, facilityId);

            int x = stm.executeUpdate();
            if (x > 0) m.setMessage("Facility with id: " + facilityId + " approved");
            else m.setMessage("Error approving facility...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    @Override
    public Message denyFacilityRequest(int facilityId) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE facility SET status='INACTIVE' WHERE status='PENDING' AND id=?"
            );
        ) {
            stm.setInt(1, facilityId);

            int x = stm.executeUpdate();
            if (x > 0) m.setMessage("Facility with id: " + facilityId + " denied");
            else m.setMessage("Error denying facility...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }
}
