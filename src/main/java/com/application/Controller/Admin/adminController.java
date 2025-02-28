package com.application.Controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("admin")
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("api/admin")
public class adminController {

    @Autowired
    private com.application.Service.adminService adminService;

    @GetMapping("/verifyLogin")
    public boolean verifyLogin(String phone, String password) {
        return adminService.verifyLogin(phone, password);
    }
}
