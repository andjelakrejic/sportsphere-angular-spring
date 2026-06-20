package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.backend.db.DB;
import com.example.backend.models.Admin;

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
}
