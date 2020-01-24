package com.ls.shiroboot.mapper;

import com.ls.shiroboot.pojo.Permission;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface PermissionMapper {

    @Select("select role_name,permission from roles_permissions where role_name=#{0}")
    Permission selectPermissionsByRole(String rolename);

}
