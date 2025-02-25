package com.application.Service;

import com.application.Object.user_credentials;
import com.application.Repository.user_credentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.Repository.userRepository;

import java.util.List;

@Service
public class userService {
    @Autowired
    private userRepository userRepository;

    @Autowired
    private user_credentialsRepository userCredentialsRepository;

    public boolean verifyLogin(String phone, String password) {
        user_credentials credentials = userCredentialsRepository.findByPhone(phone);
        if (credentials != null && credentials.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

}
