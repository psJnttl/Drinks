package base.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Role extends AbstractPersistable<Long> {
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<Account> accounts;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAccount(Account account) {
        if (null == accounts) {
            accounts = new ArrayList<Account>();
        }
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        if (null != accounts) {
            accounts.remove(account);
        }
    }
    
    @JsonIgnore
    @Override
    public boolean isNew() {
        return false;
    }

}
