package com.line.ic.svc;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.line.ic.mapper.SignInMapper;
import com.line.ic.po.SignInPo;

@Service
public class UserSvc {

    @Autowired
    private SignInMapper signInMapper;

    public boolean regist(String uname, String pwd) {
        SignInPo sign = signInMapper.selectByUname(uname);
        if (sign == null) {
            SignInPo signIn = new SignInPo();
            signIn.setCtime(new Date());
            signIn.setLastLogin(signIn.getCtime());
            signIn.setUname(uname);
            signIn.setPwd(pwd);
            return signInMapper.insert(signIn) == 1;
        } else {
            return false;
        }
    }

    public SignInPo login(String uname, String pwd) {
        SignInPo sign = signInMapper.selectByUname(uname);

        if (siginSucc(sign, pwd)) {
            SignInPo update = new SignInPo();
            update.setLastLogin(new Date());
            update.setId(sign.getId());
            signInMapper.updateByPrimaryKeySelective(update);
        }

        return sign;
    }

    private boolean siginSucc(SignInPo sign, String pwd) {
        if (sign == null) {
            return false;
        }

        return pwd.equals(sign.getPwd());
    }
}
