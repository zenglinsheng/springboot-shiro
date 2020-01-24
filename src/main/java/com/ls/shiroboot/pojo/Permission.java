package com.ls.shiroboot.pojo;

public class Permission {

    private String rolename;
    private String permission;

    public Permission(String rolename, String permission) {
        this.rolename = rolename;
        this.permission = permission;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
