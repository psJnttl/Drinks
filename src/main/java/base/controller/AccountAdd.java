package base.controller;

import java.util.ArrayList;
import java.util.List;

import base.domain.Role;

public class AccountAdd {
    private String username;
    private String password;
    private List<Role> roles;

    public AccountAdd() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = new ArrayList<>(roles);
    }

}
