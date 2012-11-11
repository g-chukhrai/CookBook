package by.chuger.cookbook.model.domain;

import javax.persistence.*;

@Entity
@Table(name = "amount", catalog = "cookbook")
public class Amount implements java.io.Serializable {

    private Integer id;
    private String name;

    public Amount() {
    }

    public Amount(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "amount_id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }
}
