package com.application.Service;

import com.application.Object.user_credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class adminService {
    @Autowired
    private com.application.Repository.adminRepository adminRepository;


    public boolean verifyLogin(String phone, String password) {
        user_credentials credentials = adminRepository.findByPhone(phone);
        if (credentials != null && credentials.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}
