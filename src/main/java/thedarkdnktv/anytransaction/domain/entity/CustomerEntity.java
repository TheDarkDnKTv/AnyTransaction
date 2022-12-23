package thedarkdnktv.anytransaction.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @GeneratedValue
    @Column( updatable = false)
    private long id;

    public CustomerEntity() {}

    public CustomerEntity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
