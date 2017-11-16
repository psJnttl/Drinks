package base.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Glass extends AbstractPersistable<Long> {

    @Column(unique=true)
    private String name;
    
    public Glass() {
    }

    public Glass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
