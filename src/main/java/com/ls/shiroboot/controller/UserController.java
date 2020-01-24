package com.ls.shiroboot.controller;

import com.ls.shiroboot.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/login")
    public String login(User user){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),
                user.getPassword());
        try {
            token.setRememberMe(user.isRememberMe());
            subject.login(token);

        }catch (AuthenticationException e) {
            return e.getMessage();
        }
        if (subject.hasRole("admin")){
            try {
                subject.checkPermission("user:update");
            }catch (AuthorizationException e){
                return e.getMessage();
            }
            return "有admin权限也有user:update";
        }

        return "无权限";
    }

    @RequestMapping("/403")
    public String error403(){
        return "403页面";
    }

    @RequiresRoles("admin")
    @RequiresPermissions("user:update")
    @RequestMapping("/index")
    public String index(){
        return "success index!";
    }

    @RequestMapping("/testRole")
    public String testRole(){
        return "success role!";
    }
    @RequestMapping("/testRole1")
    public String testRole1(){
        return "success role1!";
    }
    @RequestMapping("/testPerssion")
    public String testPerssion(){
        return "success perssion!";
    }
    @RequestMapping("/testPerssion1")
    public String testPerssion1(){
        return "success perssion1!";
    }

    //测试自定义filter
    @RequestMapping("/testMyFilter")
    public String testMyFiletr(){
        return "myFilter success";
    }
    @RequestMapping("/testMyPerssionFilter")
    public String testMyPerssionFilter(){
        return "myPerssionFilter success";
    }

}
