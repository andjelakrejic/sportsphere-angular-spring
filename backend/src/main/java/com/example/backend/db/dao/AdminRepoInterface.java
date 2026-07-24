package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Admin;
import com.example.backend.models.Message;
import com.example.backend.models.Sport;
import com.example.backend.models.User;

public interface AdminRepoInterface {
    public Admin loginAdmin(Admin a);

    public Message addSport(Sport s);

    List<User> getPendingRequests();
    public Message acceptRequest(int userId); //zahtevi za registraciju se posmatraju u "posebnom pregledu"
    public Message denyRequest(int userId);
    public List<User> viewAllAccounts();
    Message updateUser(User u);
    Message deleteUser(int userId);

    //nisi napravila na dole:
    public Message acceptFacilityRequest(int facilityId);
    public Message denyFacilityRequest(int facilityId);

}
