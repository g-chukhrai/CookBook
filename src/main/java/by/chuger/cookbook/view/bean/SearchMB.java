package by.chuger.cookbook.view.bean;

import by.chuger.cookbook.model.dao.Facade;
import by.chuger.cookbook.model.domain.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import resources.Text;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Component
@Named("searchMB")
@Scope("view")
public class SearchMB implements Serializable {

    @Autowired
    private Facade facade;
    private Collection<String> ingridients;
    private String ingridient;
    private String searchString;
    private Collection<Recipe> recipes;

    public SearchMB() {
    }

    public String getIngridient() {
        return ingridient;
    }

    public void setIngridient(String ingridient) {
        this.ingridient = ingridient;
    }

    public Collection<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Collection<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Collection<String> getIngridients() {
        if (ingridients == null) {
            ingridients = new ArrayList<String>();
        }
        return ingridients;
    }

    public void setIngridients(Collection<String> ingridients) {
        this.ingridients = ingridients;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void readParams() {
        if (Text.isNotEmpty(ingridient)) {
            String[] ingrs = ingridient.split(";");
            getIngridients().addAll(Arrays.asList(ingrs));
        }
    }

    public void search() {
        recipes = facade.getRecipeByStringAndIngridients(searchString,ingridients);
    }
}
