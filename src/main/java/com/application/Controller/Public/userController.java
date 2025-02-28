package com.application.Controller.Public;

import com.application.Object.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("sales")
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("api/public/sales")
public class userController {

    @Autowired
    private com.application.Service.userService userService;

   @Autowired
    private com.application.Service.addressService addressService;


    @GetMapping("/verifyLogin")
   public boolean verifyLogin(String phone, String password) {
       return userService.verifyLogin(phone, password);
   }

    @GetMapping("/users")
    public List<user> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{phone}")
    public user getUserByPhone(String phone) {
        return userService.getUserInfo(phone);
    }

    @PostMapping("/users")
    public user addUser(user newUser , address newAddress) {
        addressService.addAddress(newAddress, newUser.getPhone());
        return userService.createUser(newUser);
    }

    @PutMapping("/users/{phone}")
    public user updateUser(String phone, user userDetails ) {
        return userService.updateUser(phone, userDetails);
    }

    @DeleteMapping("/users/{phone}")
    public boolean deleteUser(String phone) {
        return userService.deleteUser(phone);
    }

    @GetMapping("/users/firstName/{firstName}")
    public List<user> getUsersByFirstName(String firstName) {
        return userService.getUsersByFirstName(firstName);
    }

    @GetMapping("/users/lastName/{lastName}")
    public List<user> getUsersByLastName(String lastName) {
        return userService.getUsersByLastName(lastName);
    }



    @GetMapping("/users/phone/{phone}/address")
    public List<address> getUserAddresses(String phone) {
        return addressService.getUserAddresses(phone);
    }

    @GetMapping("/users/phone/{phone}/address/{addressId}")
    public address getUserAddress(String phone, Long addressId) {
        return addressService.getUserAddress(phone, addressId);
    }


    @PutMapping("/users/phone/{phone}/address/{addressId}")
    public address updateUserAddress(String phone, Long addressId, address addressDetails) {
        return addressService.updateUserAddress(phone, addressId, addressDetails);
    }





}
