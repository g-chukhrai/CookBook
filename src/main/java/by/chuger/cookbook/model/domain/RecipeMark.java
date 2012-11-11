package by.chuger.cookbook.model.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "recipe_mark", catalog = "cookbook")
public class RecipeMark implements java.io.Serializable {

    private Integer id;
    private UserAccount userAccount;
    private Mark mark;
    private Recipe recipe;
    private Date dateAdded;

    public RecipeMark() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "recipe_mark_id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserAccount.class)
    @JoinColumn(name = "user_account_id", nullable = false)
    public UserAccount getUserAccount() {
        return this.userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Mark.class)
    @JoinColumn(name = "mark_id", nullable = false)
    public Mark getMark() {
        return this.mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Recipe.class)
    @JoinColumn(name = "recipe_id", nullable = false)
    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_added", length = 19)
    public Date getDateAdded() {
        return this.dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
