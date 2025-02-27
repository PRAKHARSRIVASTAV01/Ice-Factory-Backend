package com.application.Service;

import com.application.Object.address;
import com.application.Object.user;
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

    public user getUserInfo(String phone) {
        return userRepository.findById(phone).orElse(null);
    }

    public List<user> getAllUsers() {
        return userRepository.findAll();
    }

    public user createUser(user newUser) {
        return userRepository.save(newUser);
    }

    public user updateUser(String phone, user userDetails) {
        user user = userRepository.findById(phone).orElse(null);
        if (user != null) {
            user.setPhone(userDetails.getPhone());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            return userRepository.save(user);
        }
        return null;
    }

    public boolean deleteUser(String phone) {
        user user = userRepository.findById(phone).orElse(null);
        if (user != null) {
            userRepository.deleteById(phone);
            return true;
        }
        return false;
    }

    public boolean createUserCredentials(user_credentials newUserCredentials) {
        user_credentials credentials = userCredentialsRepository.findByPhone(newUserCredentials.getPhone());
        if (credentials == null) {
            userCredentialsRepository.save(newUserCredentials);
            return true;
        }
        return false;
    }

    public boolean updateUserCredentials(String phone, user_credentials userDetails) {
        user_credentials credentials = userCredentialsRepository.findByPhone(phone);
        if (credentials != null) {
            credentials.setPhone(userDetails.getPhone());
            credentials.setPassword(userDetails.getPassword());
            userCredentialsRepository.save(credentials);
            return true;
        }
        return false;
    }

    public boolean deleteUserCredentials(String phone) {
        user_credentials credentials = userCredentialsRepository.findByPhone(phone);
        if (credentials != null) {
            userCredentialsRepository.deleteByPhone(phone);
            return true;
        }
        return false;
    }

    public List<user_credentials> getAllUserCredentials() {
        return userCredentialsRepository.findAll();
    }

    public user_credentials getUserCredentials(String phone) {
        return userCredentialsRepository.findByPhone(phone);
    }

    public user updateUserRate(String phone, float newRate) {
        user user = userRepository.findById(phone).orElse(null);
        if (user != null) {
            user.setRate(newRate);
            return userRepository.save(user);
        }
        return null;
    }


    public user getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

   public List<user> getUsersByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    public List<user> getUsersByLastName(String lastName) {
        return userRepository.findByLastName(lastName);
    }

    public List<user> getUsersByAddress(String address) {
        return userRepository.findByAddress(address);
    }
}
