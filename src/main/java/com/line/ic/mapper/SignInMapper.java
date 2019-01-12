package com.line.ic.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.line.ic.po.SignInPo;

@Mapper
public interface SignInMapper {

    int insert(SignInPo record);

    SignInPo selectById(Integer id);

    SignInPo selectByUname(@Param("uname") String uname);

    int updateByPrimaryKeySelective(SignInPo record);

}