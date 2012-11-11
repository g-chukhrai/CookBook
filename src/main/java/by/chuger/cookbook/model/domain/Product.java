package by.chuger.cookbook.model.domain;

import javax.persistence.*;

@Entity
@Table(name = "recipe_product", catalog = "cookbook")
public class Product implements java.io.Serializable {

    private Integer id;
    private Recipe recipe;
    private Ingridient ingridient;
    private String amountSize;
    private Amount amount;

    public Product() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Amount.class)
    @JoinColumn(name = "amount_id", nullable = false)
    public Amount getAmount() {
        return this.amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Ingridient.class)
    @JoinColumn(name = "ingridient_id", nullable = false)
    public Ingridient getIngridient() {
        return this.ingridient;
    }

    public void setIngridient(Ingridient ingridient) {
        this.ingridient = ingridient;
    }

    @Column(name = "amount_size", length = 100)
    public String getAmountSize() {
        return this.amountSize;
    }

    public void setAmountSize(String amountSize) {
        this.amountSize = amountSize;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Recipe.class)
    @JoinColumn(name = "recipe_id", nullable = false)
    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
