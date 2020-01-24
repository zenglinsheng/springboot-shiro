package com.ls.shiroboot.mapper;

import com.ls.shiroboot.pojo.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {

    @Select("select username,password from users where username=#{0}")
    User selectUserByUserName(String userName);

}
