package com.ls.shiroboot.mapper;

import com.ls.shiroboot.pojo.Role;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface RoleMapper {

    @Select("select username,role_name from user_roles where username=#{0}")
    Role selectRolesByUsername(String username);

}
