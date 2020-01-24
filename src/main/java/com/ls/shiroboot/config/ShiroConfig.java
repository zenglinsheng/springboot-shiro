package com.ls.shiroboot.config;

import com.ls.shiroboot.filter.PerssionsOrFilter;
import com.ls.shiroboot.filter.RolesOrFilter;
import com.ls.shiroboot.session.CustomSessionManager;
import com.ls.shiroboot.session.RedisSessionDao;
import com.ls.shiroboot.shiro.CustomRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //不加这个注解不生效，具体不详
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    //将自己的验证方式加入容器
    @Bean
    public CustomRealm myShiroRealm(HashedCredentialsMatcher hashedCredentialsMatcher) {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return customRealm;
    }

    @Bean
    public EhCacheManager getCache(){
        return new EhCacheManager();
    }

    //权限管理，配置主要是Realm的管理认证
    @Bean
    public DefaultWebSecurityManager securityManager(Realm myShiroRealm, CustomSessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm);
        securityManager.setCacheManager(getCache());
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(RolesOrFilter rolesOrFilter, PerssionsOrFilter perssionsOrFilter, DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //自定义filter
        Map<String, Filter> filterMap = shiroFilterFactoryBean.getFilters();
        filterMap.put("myRoleFilter", rolesOrFilter);
        filterMap.put("myPerssionFilter", perssionsOrFilter);
        shiroFilterFactoryBean.setFilters(filterMap);

        //URI过滤
        Map<String, String> map = new HashMap<>();
        //自定义filter
        map.put("/testMyFilter", "myRoleFilter[admin,admin1]");
        map.put("/testMyPerssionFilter", "myPerssionFilter[user:update,user:select]");
        //登出
        map.put("/logout", "logout");
        //只有拥有相关角色查看访问链接
        map.put("/testRole", "roles[admin]");
        map.put("/testRole1", "roles[admin,admin1]");
        //只有拥有相关权限才能访问链接
        map.put("/testPerssion", "perms[user:update]");
        map.put("/testPerssion1", "perms[user:update,user:select]");
        //对所有用户认证
        map.put("/**", "authc");
        //登录
        shiroFilterFactoryBean.setLoginUrl("/login");
        //首页
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    //  加密算法
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");//采用MD5 进行加密
        hashedCredentialsMatcher.setHashIterations(1);//加密次数
        return hashedCredentialsMatcher;
    }

    //shiro注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    //自定义filter
    @Bean
    public RolesOrFilter rolesOrFilter(){
        return new RolesOrFilter();
    }
    @Bean
    public PerssionsOrFilter perssionsOrFilter(){
        return new PerssionsOrFilter();
    }

    //sessionManager
    @Bean
    public CustomSessionManager customSessionManager(SessionDAO sessionDAO) {
        CustomSessionManager sessionManager = new CustomSessionManager();
        sessionManager.setSessionDAO(sessionDAO);
        return sessionManager;
    }

    //自定义sessionDao
    @Bean
    public RedisSessionDao sessionDAO() {
        RedisSessionDao redisSessionDao = new RedisSessionDao();
        return redisSessionDao;
    }

}
