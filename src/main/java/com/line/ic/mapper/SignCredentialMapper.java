package com.line.ic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.line.ic.po.SignCredentialPo;

@Mapper
public interface SignCredentialMapper {

    int insert(SignCredentialPo record);

    SignCredentialPo selectById(Integer id);

    SignCredentialPo selectByUname(@Param("uname") String uname);

    int updateByPrimaryKeySelective(SignCredentialPo record);

}