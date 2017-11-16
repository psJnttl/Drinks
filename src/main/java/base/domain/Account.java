package base.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Account extends AbstractPersistable<Long> {
    @Column(unique=true)
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

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

    public void addRole(Role role) {
        if (null == this.roles) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }
    
    public void removeRole(Role role) {
        if (null != this.roles) {
            roles.remove(role);
        }
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    
    
}
