package com.application.Service;

import com.application.Object.admin;
import com.application.Object.user_credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class adminService {
    @Autowired
    private com.application.Repository.adminRepository adminRepository;


    public boolean verifyLogin(String username, String password) {
        admin credentials = adminRepository.findByUsername(username);
        if (credentials != null && credentials.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public void save(admin admin) {
        adminRepository.save(admin);
    }

    public void delete(admin admin) {
        adminRepository.delete(admin);
    }

    public admin findById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }


}
