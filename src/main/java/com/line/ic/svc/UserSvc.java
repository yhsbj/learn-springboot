package com.line.ic.svc;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.line.ic.mapper.SignCredentialMapper;
import com.line.ic.po.SignCredentialPo;

@Service
public class UserSvc {

    @Autowired
    private SignCredentialMapper signCredentialMapper;

    public boolean regist(String uname, String pwd) {
        SignCredentialPo sign = signCredentialMapper.selectByUname(uname);
        if (sign == null) {
            SignCredentialPo signCredential = new SignCredentialPo();
            signCredential.setCtime(new Date());
            signCredential.setLastLogin(signCredential.getCtime());
            signCredential.setUname(uname);
            signCredential.setPwd(pwd);
            return signCredentialMapper.insert(signCredential) == 1;
        } else {
            return false;
        }
    }

    public SignCredentialPo signIn(String uname, String pwd) {
        SignCredentialPo sign = signCredentialMapper.selectByUname(uname);

        if (siginSucc(sign, pwd)) {
            SignCredentialPo update = new SignCredentialPo();
            update.setLastLogin(new Date());
            update.setId(sign.getId());
            signCredentialMapper.updateByPrimaryKeySelective(update);
            return sign;
        } else {
            return null;
        }

    }

    private boolean siginSucc(SignCredentialPo sign, String pwd) {
        if (sign == null) {
            return false;
        }

        return pwd.equals(sign.getPwd());
    }
}
