package com.application.Controller.Public;

import com.application.Object.address;
import com.application.Object.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("user")
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/api/public")
public class userController {

    @Autowired
    private com.application.Service.userService userService;

   @Autowired
    private com.application.Service.addressService addressService;


    @GetMapping("/verifyLogin")
    public ResponseEntity<Boolean> verifyLogin(@RequestParam String phone, @RequestParam String password) {
        try {
            boolean result = userService.verifyLogin(phone, password);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/users")
    public ResponseEntity<List<user>> getAllUsers() {
        try {
            List<user> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/details/{phone}")
    public ResponseEntity<user> getUserByPhone(@PathVariable String phone) {
        try {
            user user = userService.getUserInfo(phone);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users/new")
    public ResponseEntity<user> addUser(@RequestBody user newUser, @RequestBody address newAddress) {
        try {
            addressService.addAddress(newAddress, newUser.getPhone());
            user createdUser = userService.createUser(newUser);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{phone}")
    public ResponseEntity<user> updateUser(@PathVariable String phone, @RequestBody user userDetails) {
        try {
            user updatedUser = userService.updateUser(phone, userDetails);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{phone}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable String phone) {
        try {
            boolean result = userService.deleteUser(phone);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/users/firstName/{firstName}")
    public ResponseEntity<List<user>> getUsersByFirstName(@PathVariable String firstName) {
        try {
            List<user> users = userService.getUsersByFirstName(firstName);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/lastName/{lastName}")
    public ResponseEntity<List<user>> getUsersByLastName(@PathVariable String lastName) {
        try {
            List<user> users = userService.getUsersByLastName(lastName);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/users/phone/{phone}/address")
    public ResponseEntity<List<address>> getUserAddresses(@PathVariable String phone) {
        try {
            List<address> addresses = addressService.getUserAddresses(phone);
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/phone/{phone}/address/{addressId}")
    public ResponseEntity<address> getUserAddress(@PathVariable String phone, @PathVariable Long addressId) {
        try {
            address address = addressService.getUserAddress(phone, addressId);
            return new ResponseEntity<>(address, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @PutMapping("/users/phone/{phone}/address/{addressId}")
    public ResponseEntity<address> updateUserAddress(@PathVariable String phone, @PathVariable Long addressId, @RequestBody address addressDetails) {
        try {
            address updatedAddress = addressService.updateUserAddress(phone, addressId, addressDetails);
            return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
