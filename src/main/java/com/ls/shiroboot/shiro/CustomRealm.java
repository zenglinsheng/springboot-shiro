package com.ls.shiroboot.shiro;

import com.ls.shiroboot.mapper.PermissionMapper;
import com.ls.shiroboot.mapper.RoleMapper;
import com.ls.shiroboot.mapper.UserMapper;
import com.ls.shiroboot.pojo.Permission;
import com.ls.shiroboot.pojo.Role;
import com.ls.shiroboot.pojo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.HashSet;

public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    {
        super.setName("customRelam");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //从数据库或缓存中获取角色数据
        Role role = roleMapper.selectRolesByUsername(userName);
        HashSet<String> roles = new HashSet<>(2);
        roles.add(role.getRolename());
        Permission permission = permissionMapper.selectPermissionsByRole("admin");
        HashSet<String> permissions = new HashSet<>(2);
        permissions.add(permission.getPermission());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1、从主体传过来的认证信息中，获得用户名
        String userName = (String) authenticationToken.getPrincipal();

        //2、通过用户名到数据库中获取凭证
        User user = userMapper.selectUserByUserName(userName);
        if (user == null) {
            return null;
        }
        String password = user.getPassword();
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo("linsheng", password, "customRealm");
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("linsheng"));

        return simpleAuthenticationInfo;
    }


}
