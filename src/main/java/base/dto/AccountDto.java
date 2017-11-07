package base.dto;

import java.util.ArrayList;
import java.util.List;

import base.domain.Role;

public class AccountDto {
    private String username;
    private String password;
    private List<Role> roles;

    public AccountDto(String username) {
        this.username = username;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((roles == null) ? 0 : roles.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AccountDto other = (AccountDto) obj;
        if (password == null) {
            if (other.password != null) return false;
        }
        else if (!password.equals(other.password)) return false;
        if (roles == null) {
            if (other.roles != null) return false;
        }
        else if (!roles.equals(other.roles)) return false;
        if (username == null) {
            if (other.username != null) return false;
        }
        else if (!username.equals(other.username)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "AccountDto [username=" + username + ", roles=" + roles + "]";
    }

}
