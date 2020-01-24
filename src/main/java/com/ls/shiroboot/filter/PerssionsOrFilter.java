package com.ls.shiroboot.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class PerssionsOrFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        String[] perssions = (String[]) mappedValue;
        if (perssions == null || perssions.length == 0){
            return true;
        }
        for(String perssion: perssions){
            if (subject.isPermitted(perssion)){
                return true;
            }
        }
        return false;
    }
}
