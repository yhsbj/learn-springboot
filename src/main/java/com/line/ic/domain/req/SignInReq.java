package com.line.ic.domain.req;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

public class SignInReq {

    @NotNull
    private String uname;
    private String pwd;
    private List<String> list;

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date curr = new Date();

    public Date getCurr() {
        return curr;
    }

    public void setCurr(Date curr) {
        this.curr = curr;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

}
