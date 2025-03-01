package com.application.Controller.Admin;

import com.application.Object.admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("admin")
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("api/admin")
public class adminController {

    @Autowired
    private com.application.Service.adminService adminService;

    @GetMapping("/verifyLogin")
    public ResponseEntity<Boolean> verifyLogin(@RequestBody admin admin) {
        try {
            boolean verifiedAdmin = adminService.verifyLogin(admin);
            return new ResponseEntity<>(verifiedAdmin, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
