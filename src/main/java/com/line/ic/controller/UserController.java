package com.line.ic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.line.ic.po.SignInPo;
import com.line.ic.svc.UserSvc;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserSvc userSvc;

    @GetMapping("test")
    private String test() {
        return "Hello User!";
    }

    @PostMapping("regist")
    private String regist(String uname, String pwd) {
        boolean b = userSvc.regist(uname, pwd);
        return String.valueOf(b);
    }

    @GetMapping("signIn")
    private SignInPo signIn(String uname, String pwd) {
        SignInPo sign = userSvc.login(uname, pwd);
        if (sign != null) {
            sign.setPwd(null);
        }
        return sign;
    }

}
