package by.chuger.cookbook.model.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mark", catalog = "cookbook")
public class Mark implements java.io.Serializable {

    private Integer id;
    private Double value;
    private String description;
    private Set<RecipeMark> marks = new HashSet<RecipeMark>(0);

    public Mark() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "mark_id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "value", nullable = false)
    public Double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Column(name = "description", length = 10)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mark", targetEntity = RecipeMark.class)
    public Set<RecipeMark> getRecipeMarks() {
        return this.marks;
    }

    public void setRecipeMarks(Set<RecipeMark> marks) {
        this.marks = marks;
    }
}
