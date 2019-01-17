package com.line.ic.controller;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.line.ic.domain.req.SignInReq;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private TestSvc svc;

    @GetMapping("ano")
    private String test(@Valid SignInReq req) {
        return "Hello User!";
    }

    @GetMapping("ano1")
    private String test1(String uname) {
        // return "Hello test1!";
        if (1 == 1) {
            throw new IllegalArgumentException("haa");
        }
        SignInReq req = new SignInReq();

        return svc.hi(req);
    }

    @GetMapping("ano2")
    private String test2(@Validated @Size(min = 8, max = 11) String uname) {
        return "Hello test2!";
    }

    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(TestController.class);
        log.info("test info");
        log.error("test info");
    }
}
