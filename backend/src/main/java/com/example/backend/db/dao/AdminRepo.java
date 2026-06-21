package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.backend.db.DB;
import com.example.backend.models.Admin;
import com.example.backend.models.Message;
import com.example.backend.models.Sport;

public class AdminRepo implements AdminRepoInterface{
    
    @Override
    public Admin loginAdmin(Admin a) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("select * from admin where username=? and password=?");
        ) {
            stm.setString(1, a.getUsername());
            stm.setString(2, a.getPassword());

            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return new Admin(
                    rs.getString("username"),
                    rs.getString("password")    
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message addSport(Sport s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addSport'");
    }

    @Override
    public Message acceptRequest() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'acceptRequest'");
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
