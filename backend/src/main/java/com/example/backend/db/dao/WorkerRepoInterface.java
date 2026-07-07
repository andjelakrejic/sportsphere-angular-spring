package com.example.backend.db.dao;

import org.springframework.web.multipart.MultipartFile;

import com.example.backend.models.Message;
import com.example.backend.models.Worker;
import com.example.backend.models.helpers.ChangePasswordObject;

public interface WorkerRepoInterface {

    public Worker loginWorker(Worker a);
    public int registerWorker(Worker w);
    public String uploadProfileImage(String username, MultipartFile image);
    public Worker getWorker(int id);
    public Message updateWorker(Worker w);

    public boolean usernameExists(String username);
    public boolean emailExists(String email);
    public Message changePassword(ChangePasswordObject obj);

    public int countWorkersAtFacility(String facilityName, String address);
    public boolean maticniBrojExists(String mb);
    public boolean pibExists(String pib);
}
