package com.ls.shiroboot;

import com.ls.shiroboot.mapper.RoleMapper;
import com.ls.shiroboot.mapper.UserMapper;
import com.ls.shiroboot.pojo.Role;
import com.ls.shiroboot.pojo.User;
import com.ls.shiroboot.utils.JedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Set;

@SpringBootTest
public class ShiroBootApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private JedisUtil jedisUtil;

    @Test
    public void selectUserByUserName(){
        User user = userMapper.selectUserByUserName("linsheng");
        System.out.println(user.getUsername()+user.getPassword());
    }

    @Test
    public void selectRolesByUsername(){
        Role role = roleMapper.selectRolesByUsername("linsheng");
        System.out.println(role.getRolename());
    }

    /*@Test
    public void jedisTest(){
        String name = "name";
        jedisUtil.set(name, "linsheng");
        String myName = (String) jedisUtil.get(name);
        System.out.println(myName);
    }*/

}
