package com.application.Controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("admin")
public class adminConntroller {

    @Autowired
    private com.application.Service.adminService adminService;

    @GetMapping("/verifyLogin")
    public boolean verifyLogin(String phone, String password) {
        return adminService.verifyLogin(phone, password);
    }
}
