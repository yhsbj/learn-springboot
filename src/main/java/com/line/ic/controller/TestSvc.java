package com.line.ic.controller;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.line.ic.domain.req.SignInReq;

@Service
@Validated
public class TestSvc {

    public String hi(@Valid SignInReq req) {
        return "hi: " + req.getUname();
    }

    public String hi(@Size(min = 8, max = 11) String uname) {
        return "hi: " + uname;
    }
}
